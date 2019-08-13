# Concurrency


1. [Concept of concurrency and Java thread](#chapter-1---Concept-of-concurrency) ![Progress](http://progressed.io/bar/100)

2. [Java thread](#chapter-2---java-thread-usage) ![Progress](http://progressed.io/bar/100)

3. [RxJava](#chapter-3---rxjava) ![Progress](http://progressed.io/bar/0)

4. [Kotlin Coroutine](#chapter-4---kotlin-coroutine) ![Progress](http://progressed.io/bar/32)

----
## Chapter 1 - Concept of concurrency

### Process 란?

<img src="https://www.lifewire.com/thmb/oZ-AvDqj9BBr8gKjGA9ammL7CAc=/768x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/2017-06-19-59483de05f9b58d58affe9eb.png" width=400>

* 컴퓨터에 설치된 프로그램들이 실행되어 메모리에 올려진 상태이다.

* CPU에 있는 코어 개수에 따라 동시에 실행시킬 수 있는 프로세스의 개수가 다르다.

* 프로세스들이 Context-Switching을 통해 번갈아가며 실행되기 때문에 마치 실행되고 있는 프로그램들이 동시에 작동하는 것처럼 느껴진다.




### Thread 란?

<img src="https://i.stack.imgur.com/IwE9m.png" width=400>

* 프로세스 내에 존재하는 컴퓨터 자원이며, 한 프로세스 내에서 프로그래밍적으로는 독립적으로 실행되는 코드를 의미한다.

* 프로세스와 달리 메모리를 공유한다 == 다른 쓰레드와 변수 같은 것들을 공유할 수 있다.




### 동기(synchronous) 작업 vs 비동기(asynchronous) 작업


* 동기 코드

```kotlin
fun main(){
    //5초를 기다려야 한다.
    //만약 안드로이드의 메인 쓰레드라면 5초동안 앱이 멈춘 것 처럼 보인다.
    longTask()
}


fun longTask() : Int {
    Thread.sleep(5000)
    return 10
}
```

* 비동기 코드

```kotlin
fun main(){
    //5초를 기다리지 않고, 콜백을 전달해서 작업이 완료되면 결과를 전달받는다.
    longTask {result:Int->

    }
}


fun longTask(callback : (Int) -> Unit) {
    thread {
        Thread.sleep(5000)
        callback(10)
    }
}
```

* 어플리케이션에서는 비동기 작업을 해주기 위해 메인 쓰레드가 아닌 다른 쓰레드를 만들어서 이용해주어야 한다.


다음과 같은 비디오를 다운로드 받는 함수가 동기, 비동기로 구현이 되어있다.

```kotlin
private fun downloadVideoSync(): Video {
    Thread.sleep(5000)
    val video = Video("Sync Video", 5000)
    return video
}

private fun downloadVideoAsync(callback: (Video) -> Unit) {
    thread {
        Thread.sleep(5000)
        val video = Video("Async Video", 5000)
        callback(video)
    }
}
```

버튼을 눌렀을 때 각각 작업을 실행하고 TextView 에 작업 결과를 보여주고 싶다.

```kotlin
/**
 * 동기화 코드를 이용한 비디오 다운로드 및 UI에 표시하기
 */
syncDownButton.setOnClickListener {
    val video = downloadVideoSync()
    syncDownResultTextView.text = video.toString()
}

/**
 * 비동기화 코드와 콜백 패턴을 이용한 비디오 다운로드 및 UI에 표시하기
 * 
 * Main Thread에서만 UI작업을 해줄 수 있기 때문에 코드가 많이 더러워진다.
 * 
 * 
 */
asyncDownButton.setOnClickListener {
    downloadVideoAsync { video->
        val message = Message.obtain()
        message.obj = video

        val handler = Handler(Looper.getMainLooper()) {message->
            
            val video = message.obj as Video

            asyncDownResultTextView.text = video.toString()

            true
        }

        handler.sendMessage(message)

    }
}
```

사실 비동기 코드를 조금 극단적으로 보여주기 위해 길게썼는데 다음과 같이 단축이 가능하다.(그래도 김)

```kotlin
asyncDownButton.setOnClickListener {
    downloadVideoAsync { video ->
        activity?.runOnUiThread {
            asyncDownResultTextView.text = video
        }
    }
}
```

게다가 이 비동기 작업에서의 콜백 패턴이 길어지면 다음과 같은 사태가 벌어질 수도 있다.


<img src="https://pbs.twimg.com/media/Bp4QbckCcAA9EKS.jpg" width=600>



안드로이드에서(iOS도 마찬가지) UI 관련 변경은 오직 메인 쓰레드에서만 가능하다.

그래서 콜백 패턴을 이용해 Video 객체를 받아왔음에도 불구하고, 메인 쓰레드로 다시 Video 객체를 보내줘서 UI 업데이트를 해야한다.

* 메인 쓰레드에서 동기 코드의 문제점 => 코드가 실행되는 동안 앱이 멈춘 것처럼 보인다(ANR 상태 = Application Not Response)

* 비동기 코드의 문제점 => 쓰레드 관리가 까다롭고, 적절한 시기에 취소시켜주기도 어려우며, 작성해야 할 코드가 많아진다.

* Kotlin Coroutine => 메인 쓰레드에서 동기 코드를 써도 비동기처럼 실행됨

* RxJava => 비동기 코드를 쉽고 강력하게 작성할 수 있게 도와줌


### Concurrency 란?

[동시에 두 개 이상의 쓰레드가 동작이 가능합니까?](https://stackoverflow.com/questions/19324306/running-two-threads-at-the-same-time)

[Concurrency vs Parallelism](https://www.quora.com/What-is-concurrency-in-programming)


* 컴퓨터공학적으로, 어떠한 독립적인 작업이 동시에 이루어지는 것 => Parallelism

* 컴퓨터공학적으로, 어떠한 독립적인 작업이 동시에 이루어지는 것 처럼 보이지만, 순차적으로 실행되는 것 => Concurrency


----
## Chapter 2 - Java thread usage


### Thread

Java에서 쓰레드를 다루는 클래스이다.

다음과 같이 사용이 가능하다.

```java
public class App {

    public static void main(String[] args) {
        Thread myThread = new MyThread();

        myThread.start();

        System.out.println("내 쓰레드가 시작됐다!");
    }
}

class MyThread extends Thread  {
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        }catch(InterruptedException ex) {

        }
        System.out.println("Hi I am new Thread!");
    }
}
```

Thread 를 상속해서, run 메서드를 오버라이딩 해서 원하는 작업을 하고, 우리가 만든 쓰레드 클래스의 객체를 만들어서 start 메서드를 호출함으로써 쓰레드가 시작된다.

```
내 쓰레드가 시작됐다!
Hi I am new Thread!
```

### Runnable

실행될 수 있는 코드블록을 의미하는 인터페이스이다.

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {

    }
}
```

쓰레드를 만들 때, 인자로 Runnable 을 전달해주어서 쓰레드에서 실행할 작업을 정의해줄 수도 있다.

```java
public static void main(String[] args) {
    Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() {
            //...
        }
    });
    
    myThread.start();

    System.out.println("내 쓰레드가 시작됐다!");
}
```

익명 클래스로 Runnable 객체를 만들어서 Thread 클래스의 생성자로 전달해준 모습이다.

코틀린에서 쓰레드는 다음과 같이 쉽게 사용이 가능하다.

```kotlin
thread {
    println("코틀린 은 thread 블록으로 해결")
}
```

그냥 thread 라는 키워드만 붙이고 실행될 블록만 넣어주면 알아서 실행된다.

이건 사실 내부적으로 Java의 Thread 클래스를 사용하는 것이다.



### Handler & Looper & Message

Thread와 Runnable 같은 것들이 자바라는 언어에 존재하는 쓰레드를 다루는 클래스들이라고 한다면, Handler 는 안드로이드 프레임워크에 존재하는 것이다.

<img src="https://mindorks.files.wordpress.com/2018/01/0392b-1xr9n1cbqsnqahnpuuhylhq.jpg" width = 600>

[안드로이드 프레임워크의 쓰레드 관리 클래스들 - 매우 유익](https://blog.mindorks.com/android-core-looper-handler-and-handlerthread-bd54d69fe91a?fbclid=IwAR0n4ywKxZBjfL2_hHxhHokwnC4CL8MTSiOfOtqr9EnXidYyCCCXFUXwi1E)


Looper와 Handler는 뗄 수 없는 개념인데, Thread 클래스만 써서 쓰레드를 사용할 수 있지만, 이 것들이 존재하는 이유는 동일한 쓰레드를 이용해 특정한 작업을 계속 처리해주고 싶을 때가 있기 때문이다.

예를 들어, Main Thread에서 UI 업데이트를 한번만 할 것은 아니기 때문에 계속 한 Thread를 살려놓고 특정 작업을 할 수 있게 해주는 것이다.

Looper는 우리가 반복문 Loop 할때 쓰는 그 Loop 이다.

예를 들어 Looper는 내부적으로 다음과 같은 코드로 이루어져 있을 것이다.


```kotlin
while(true) {
    findAnyNewWorkAndExecute()
}
```

계속 반복문을 돌면서, 처리해야할 작업이 있는 지 찾고, 있다면 해당 쓰레드에게 작업을 하라고 명령해주는 역할이다.

처리해야할 작업이 새롭게 오면, Message Queue에 그 작업을 넣고, 순차적으로 실행한다.

Handler 는 하나의 Looper에 여러 개가 존재할 수 있으며, 이 Looper를 이용해서 쓰레드를 어떻게 사용할 지 메세지를 보내주거나 하는 역할이다.

그래서 우리가 백그라운드 쓰레드에서 Main Thread 에서 실행되는 코드를 실행시켜주고 싶다면, 다음과 같이 코드를 작성해야 하는 것이다.

```kotlin
thread {
    //이 코드는 백그라운드 쓰레드에서 실행됩니다.
    Handler(Looper.getMainLooper()).post(object : Runnable {
        override fun run() {
            //이 코드는 메인 쓰레드에서 실행됩니다.
        }
    })
}
```

**Looper.getMainLooper** 는 메인 쓰레드의 Looper를 건내준다.





### HandlerThread

우리가 기본적인 Thread를 만들면 한번밖에 못사용하기 때문에, Looper가 를 해당 쓰레드에서 돌려야 한다.

Looper가 돌아야 Message Queue도 생성되어서 쓰레드가 계속 살아있으면서 명령을 대기하게 된다.

이를 간편하게 도와주는 것이 HandlerThread 클래스이다.

```kotlin
val thread = HandlerThread("My Handler Thread")
thread.start()

val handler = Handler(thread.looper) //Error

handler.post {
    //이 코드는 우리의 MyThreadWithLooper 객체의 쓰레드에서 실행됩니다.
}
```

HandlerThread 로 만든 객체는 Looper(또한 Message Queue) 를 갖고있기 때문에 이 Looper를 Handler의 생성자로 전달하면, 핸들러를 이용해 계속 해당 쓰레드에서 원하는 작업을 시켜줄 수 있다.



----
## Chapter 3 - RxJava

* 추후 블로그에 처음부터 자세히 포스팅 예정

----
## Chapter 4 - Kotlin Coroutine

* [블로그](http://mym0404.blog.me/221603580011)에 포스팅 중(미완)
