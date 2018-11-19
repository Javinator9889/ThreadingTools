# ThreadingTools
Java useful tools for managing threads like a master ⚡👊🛠


## 1. Introduction and purposes

### `ThreadsPooling`
*ThreadsPooling* provides a <b>fast, easy</b> access to a pool of threads that concurrently
must be running, with upper limits.

It accepts **all types of** `Runnable` classes:

+ `Thread`.
+ `NotifyingThread`.
+ Any class that implements `Runnable`.

*ThreadsPooling* aims to be **as helpful as possible** for the developer: with some simple 
methods, anyone can create a new *ThreadsPooling* object with all the values set-up to their 
defaults:

+ **Concurrent running process**: its default value is `4`.
+ **Maximum running process**: its default value is `8`.
+ **Default keep alive time**: its default is `100 ms`.
+ **Default queue capacity**: its default is `100` items.

For more information, please read the 
**[official documentation](https://javinator9889.github.io/ThreadingTools)**.

### `NotifyingThread`

*NotifyingThread* provides an specialised class on **threading** that adds more options to the 
currently available ones:

+ **Notifying**: sometimes, we want to get notified when a `Thread` completes its execution, but 
actually the most we can do is just call `join()` and wait until its completion, making us unable
 to **do other works**. <br />
 `NotifyingThread` provides a **fast, powerful** class for getting notified when, the *threads we
  want*, finish. The only requirement is to **subscribe** our class to the *listener* classes, 
  just by implementing `OnThreadCompletedListener`.
  
+ **Fast development**: is very useful to create **nested classes** inside a thread by 
declaring:<br />
    ```java
    new Thread(new Runnable() {
        // ALL THE CODE TO BE EXECUTED
    }).start();
    ```
    Using the **powerful** lambda expressions, doing this with the code is not more necessary. 
    For example:
    ```java
    public class MyClass implements OnThreadCompletedLisener {  
      private int myField;
      
      /* --- CONSTRUCTORS,ETC. --- */
    
      public void heavyOperationThatUsesLotsOfResources() {
          // The big operation - imagine that uses myField value
          // This operation also takes about 5 minutes to complete,
          // so we do not want to wait all that time.  
      }
    
      public void caller() {
          NotifyingThread thread = new NotifyingThread(this);
          // "this" refers itself for the "OnCompletedListener"
          thread.setExecutable(this::heavyOperationThatUsesLotsOfResources);
          // by using that expression, we do not need to write again
          // the hole function.
          thread.start();
      }
    
      public void onThreadCompletedListener(final Thread thread, Throwable exception) {
          // Handle thread finish
          if (exception != null)
              System.out.println("Thread " + thread.getName() + " finished!");
          else 
              System.err.println("Thread " + thread.getName() + " finished with an exception");
      }
    }
    ```
    As you can see, we declared and used the **function inside our class** only with one line, by
     using a lambda expression.
     
+ **Adaptive**: by using the `ArgumentParser` and the overloaded methods of `setExecutable`, 
`NotfyingThread` adapts to each function you need to use at every moment.

For more information, please read the 
**[official documentation](https://javinator9889.github.io/ThreadingTools)**.

### `ArgumentParser`

`ArgumentParser` provides a <b>fully compatible</b> class with <i>plenty of objects</i>. It
is designed for using it as an <b>access platform</b> to methods' arguments and params, taking
advantage of *lambda expressions* of Java 8 and above.

It is not <b>thread safe</b> as all the operations are not done <i>atomically</i>, so there is no
guarantee that all the data stored at `HashMap` is saved in the order expected and with the
expected values if appending from multiple threads at the same time.

It is based on **ContentValues**, used in Android, with some *customizations* and new tools.

For more information, please read the 
**[official documentation](https://javinator9889.github.io/ThreadingTools)**.


## 2. Installation

For using this library at **any Java application you are using**, you can just *[download from 
"Releases"](https://github.com/Javinator9889/ThreadingTools/releases)* or use one of the 
following methods:

### Maven
*First add JCenter to your app*:
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```

Then, you can just include the lib:
```xml
<dependency>
  <groupId>com.github.javinator9889</groupId>
  <artifactId>threadingtools</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```

### Gradle
*First, add JCenter to your app*:
```groovy
repositories {
    jcenter()
    // Other repositories you have
}
```

Then, you can just include the lib:
```groovy
implementation 'com.github.javinator9889:threadingtools:1.0'
```

### Ivy
*First, add JCenter to your Ivy settings*:
```xml
<ivysettings>
    <resolvers>
        <ibiblio name="bintray"
                 m2compatible="true"
                 root="http://dl.bintray.com/content/example/external-deps"/>
    </resolvers>
</ivysettings>
```

Then, you can just include the lib:
```xml
<dependency org='com.github.javinator9889' name='threadingtools' rev='1.0'>
  <artifact name='threadingtools' ext='pom' ></artifact>
</dependency>
```

You must have to **include JCenter()** in order to make it work.

## 3. Usage

After [successfully included the library in your project](#2-installation), you must do the following for using this class:
+ Generate a new `ThreadsPooling` if you are going to use them.
+ Setup the `NotifyingThreads`, and include them inside the `ThreadsPooling`.
+ Call `ThreadsPooling.start()` method for start executing the threads.

You can see some examples at the [test folder](https://github.com/Javinator9889/ThreadingTools/threadingtools/src/test/java), in which all those process and lambda usage are
 specified.
 

## 4. Contributing

If you find any error or you want to **add a new feature**, you can perfectly:
1. Open a **[new issue](https://github.com/Javinator9889/ThreadingTools/issues)** completing
 the *issue template* so it will be easier to solve it.
 
2. Create a new **[pull request](https://github.com/Javinator9889/ThreadingTools/pulls)** 
with the changes you have made to the project, and waiting my approval for merging them.

3. Give me a **star** ⚡⭐ if you found this library useful 😄
 
## 5. License
 
     Copyright © 2018 - present | Javinator9889
 
     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     (at your option) any later version.
 
     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
 
     You should have received a copy of the GNU General Public License
     along with this program.  If not, see https://www.gnu.org/licenses/.
