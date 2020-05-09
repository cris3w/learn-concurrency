package com.examples

class Counter {

  private var value = 0

  def getValue: Int = value

  def increment(): Unit = value += 1
}

class Increment(counter: Counter, n: Int) extends Runnable {

  override def run(): Unit =
    for (_ <- 0 until n)
      counter.increment()
}

// A data race is a situation in which
// at least two threads access a shared variable at the same time
// and at least one thread tries to modify the variable.

object DataRaceExample extends App {

  private val NumThreads = 4
  private val NumIncrements = 1000

  val threads = new Array[Thread](NumThreads)
  val counter = new Counter()

  for (i <- 0 until NumThreads) {
    threads(i) = new Thread(new Increment(counter, NumIncrements))
    threads(i).start() // begins execution
  }

  for (i <- 0 until NumThreads)
    threads(i).join() // waits for this thread to die

  println(s"TOTAL = ${counter.getValue}")
  println(s"EXPECTED = ${NumThreads * NumIncrements}")
}
