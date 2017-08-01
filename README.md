# ProjectExpDemo

### JAVA
* 使用NIO代替字节流等，速度在两倍以上
* 使用SparseArray代替hashMap
<pre>
  1、如果key的类型已经确定为int类型，那么使用SparseArray，因为它避免了自动装箱的过程，如果key为long类型，它还提供了一个LongSparseArray来确保key为long类型时的使用
  2、如果key类型为其它的类型，则使用ArrayMap
 </pre>

### Android
* 使用BaseActivity来管理Activity等（可判断Activity是否在运行）
* CommonAdapter来做万能适配器
* 使用Eventbus,可以使用粘性消息
* 善于使用launchMode，savedInstanceState等来保存信息
* 使用ThreadHandler做线程（有队列的功能）//TODO 线程停止使用interrpted ,再使用标志位。
* 使用lock,await,signal,ArrayBlockingQueue等JDK1.5特性
<pre>  
    http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0907/3428.html
    join方法用线程对象调用，如果在一个线程A中调用另一个线程B的join方法，线程A将会等待线程B执行完毕后再执行。
    yield可以直接用Thread类调用，yield让出CPU执行权给同等级的线程，如果没有相同级别的线程在等待CPU的执行权，则该线程继续执行。
</pre>
* 构造函数 构造代码块 静态代码块 Java类初始化顺序，可以在静态代码库中加载一次性东西
* 内存分析工具 MAT 的使用
* http://www.lightskystreet.com/2015/09/01/mat_usage/


