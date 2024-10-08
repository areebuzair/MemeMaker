![MemeMaker Logo](/MemeMaker/icon.png "MemeMaker Logo")

MemeMaker
=========

### An ultra-fluid image editor for making memes

#### Made by:
1. [J. M Areeb Uzair](https://github.com/areebuzair)
2. [Nirjon Talukder](https://github.com/nrzn77)
3. [Fardeen Sadab](https://github.com/fardeensadab)

#### Contents:
1. [UI Description](#UI-Description)
2. [View all hotkeys and mouse shortcuts](#shortcuts)
3. [Compile and Run the source code](#compile-and-run-source-code)

> [!NOTE]
> In the following text, _objects_ refer to _images_ or _texts_ placed on the canvas.  

> [!WARNING]
> This application requires a mouse to be run.
> Making any object excessively large will cause the application to freeze. Stay within a 4000 \* 4000 resolution canvas size.

Reposition, resize images and add texts with minimum effort! Meme maker is an app to put together memes in the quickest possible time. It allows fast resizing and repositioning of texts and images, and also a fluid resizing feature for the image canvas. Only the canvas area is renderable.
  
## UI Description

![The App UI](UI.jpg "User Interface")

#### Assets:

From here, select a folder that will display all the images in that folder. Click on any image to add it to the canvas. Clicking default will open the inbuilt template library.

#### Add Text

Clicking this button will add a Text to the canvas.

#### Canvas Settings

Choose the background colour, width and height of the canvas. 
> [!IMPORTANT]
> After setting the width or height, ENTER has to be pressed.

#### Save Image

Save the canvas as an image. Only the parts inside the canvas will be saved.

* * *

## Shortcuts

What sets MemeMaker apart is its multitudes of shortcuts. These are:

#### Select an object:

Left-Click or Right-Click. Selecting a Text object will open the text settings on the left menu, enabling user to edit the text-content, font and font-colour.

#### Move an object:

Left Mouse Button drag to move an object. Holding down SHIFT will soft-lock the movement to the X or Y axis.

#### Resize an object:

Right Mouse Button drag to resize an object. While resizing an image, holding down SHIFT will lock its aspect ratio. While resizing a text, vertical drag will change its font-size.

#### Delete an object:

Pressing BACKSPACE or DELETE will delete any selected object.

#### _Push an object behind its predecessor:_

By clicking an object while pressing ALT, the object will be pushed behind the object which was originally behind it.

#### Pan the canvas:

Middle Mouse Button drag to move the canvas around.

#### Resize the canvas:

Middle Mouse Button drag while holding SHIFT will resize the canvas.

#### Bring the canvas into view:

Pressing HOME will reset the zoom of the canvas and also bring it into view.

#### Zoom in/out:

Scrolling will cause the canvas to zoom in or out.

#### Saving:

Press CONTROL+s

#### Accessing the documentation:

Press CONTROL+h  

* * *

## Compile and Run Source Code

This code is written using Java 19.0.1 and JavaFX version 21.0.2 on Windows Visual Studio code.

To compile and run the source code, run the following commands in the command prompt after installing Java and JavaFX:
```
javac --module-path "PathToLib" --add-modules=javafx.controls,javafx.fxml,javafx.swing src\MemeMaker\*.java -d classes
copy src\MemeMaker\*.fxml classes\MemeMaker\*.fxml
copy src\MemeMaker\*.png classes\MemeMaker\*.png
copy src\MemeMaker\*.css classes\MemeMaker\*.css
md app
jar --create --file=app/MemeMaker.jar --main-class=MemeMaker.Main -C classes .
java --module-path "PathToLib" --add-modules=javafx.controls,javafx.fxml,javafx.swing -jar app/MemeMaker.jar
```
> [!IMPORTANT]
> Replace "PathToLib" with the location to the lib folder of your JavaFX SDK. It should look something like: C:\downloads\javafx-sdk-21.0.2\lib
  
Thanks from Team Meme Addicts.
