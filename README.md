# Project Mobile Application

Source code can be found [>> here <<](https://github.com/Antoinetest01/Project_Mobile_Application/releases/tag/1.0.0)

## Important to download to get the code working:

- SDK and CMake :

Go to :
Tools -> SDK Manager -> SDK Tools

Then check the boxes : 
- NDK (Side by side)
- CMake  

And click on "Ok"

![image](https://user-images.githubusercontent.com/78219632/110218502-6b0d9080-7eba-11eb-9a28-d147ce6ffe4b.png)

## Explain how you ensure user is the right one starting the app

The application needs a password to be unlocked. The first time you log in to the project, you just have to type a password that you can memorize because it will be stored and kept in a room database that will check each time you log in if the user enters the password you set at the beginning. 

The code is not kept in the cache or in RAM and only its SHA-1 hash is stored on the phone.

## How do you securely save user's data on your phone ?

By passing the password the first time, the code stores the password in a room database. A room database stores the data in a sqlite file in app internal data directory which means that no other application is able to access it. Only the root user can access the data.

## How did you hide the API url ?

The main idea of our implementation is to store the URL of the API in a room database. To do so, the code stores the URL in a C++ class using native JNI code to hide sensitive string data with SDK and Cmake. Since C++ classes are stored inside it means that it is very difficult to decode it. 

## Screenshots of your application

![image](https://user-images.githubusercontent.com/78219632/110247288-c3e93180-7f6b-11eb-8fe4-342e5f397bab.png)

![image](https://user-images.githubusercontent.com/78219632/110247258-a916bd00-7f6b-11eb-9907-b99f10ce6344.png)







