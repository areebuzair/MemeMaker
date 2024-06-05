This code is written using Java 19.0.1 and JavaFX version 21.0.2 on Windows Visual Studio code.

To compile and run the source code, run the following commands in the command prompt after installing Java and JavaFX:

  javac --module-path "PathToLib" --add-modules=javafx.controls,javafx.fxml,javafx.swing src\MemeMaker\*.java -d classes
  copy src\MemeMaker\*.fxml classes\MemeMaker\*.fxml
  copy src\MemeMaker\*.png classes\MemeMaker\*.png
  copy src\MemeMaker\*.css classes\MemeMaker\*.css
  md app
  jar --create --file=app/MemeMaker.jar --main-class=MemeMaker.Main -C classes .
  java --module-path "PathToLib" --add-modules=javafx.controls,javafx.fxml,javafx.swing -jar app/MemeMaker.jar

> [IMPORTANT]
> Replace "PathToLib" with the location to the lib folder of your JavaFX SDK. It should look something like: C:\downloads\javafx-sdk-21.0.2\lib

