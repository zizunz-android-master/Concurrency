# Concurrency


1. [Concept of concurrency and Java thread](#chapter-1---Concept-of-concurrency)

2. [Java thread](#chapter-2---java-thread-usage)

3. [RxJava](#chapter-3---rxjava)

4. [Kotlin Coroutine](#chapter4---kotlin-coroutine)

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

### Concurrency 란?

[동시에 두 개 이상의 쓰레드가 동작이 가능합니까?](https://stackoverflow.com/questions/19324306/running-two-threads-at-the-same-time)

[Concurrency vs Parallelism](https://www.quora.com/What-is-concurrency-in-programming)


* 컴퓨터공학적으로, 어떠한 독립적인 작업이 동시에 이루어지는 것 => Parallelism

* 컴퓨터공학적으로, 어떠한 독립적인 작업이 동시에 이루어지는 것 처럼 보이지만, 순차적으로 실행되는 것 => Concurrency


----
## Chapter 2 - Java thread usage



----
## Chapter 3 - RxJava



----
## Chapter 4 - Kotlin Coroutine
