# Media Editor Project

## Overview

The Media Editor project is a web application designed for video, image processing tasks. It offers various features including:

## Technologies Used

- **Mostly Java Core Utilities**: Utilized for fundamental functionalities in Java programming.
- **java.io**: Employed for input and output operations, including file handling.
- **java.util.concurrent**: Utilized for managing concurrent processing and threading.
- **Spring Boot MVC**: Used for building and deploying Java-based applications.
- **Spring Framework HTTP**: Utilized for handling HTTP requests and responses in the Spring framework.
- **Spring Web Multipart**: Used for handling multipart/form-data requests, commonly used in file uploads.
- **FFmpeg**:from C language library Integrated for video processing tasks, such as resizing and extracting audio.

## Completed task
1. **Resize Video**: Resize uploaded videos to specified dimensions.
2. **Upload Video**: Upload videos to the server.
3. **Extract Audio**: Extract audio from uploaded videos.
4. **User Authentication and Update profile**: Login and logout functionalities.
5. **Download VIDEO and AUDIO**: download from uploaded and extracted audio, video.

## Features

1. **Resize Image**: 
2. **Upload Image**:
4. **Advanced User Authentication**: 

## Installation

To get started with the Media Editor project, follow these steps:

### Prerequisites

- **Git**: Ensure you have Git installed on your machine.
- **Java**: Make sure you have JDK installed.
- **Gradle**: Build tool for running the project.
- **Ffmpeg**: FFmpeg is a free and open-source software project consisting of a suite of libraries and programs for handling video, audio, and other multimedia files and streams. At its core is the command-line ffmpeg tool itself, designed for processing of video and audio files.

### Steps
java-media-editor git:(main) git push -u origin main Username for 'https://github.com': tungducng Password for 'https://tungducng@github.com': how to set default username and password for i dont need press it again anymore
1. **Clone the Repository**:
    ```sh
    git clone https://github.com/tungducng/media-editor-java.git
    cd media-editor-java
    ```

2. **Install FFmpeg**:
   FFmpeg is required for video processing tasks.
    ```sh
    sudo apt install ffmpeg
    ```

3. **Build the Project**:
   Use Gradle to build the project.
    ```sh
    gradle build
    ```

4. **Run the Application**:
   Start the application using Gradle.
    ```sh
    gradle bootRun
    ```
####################################################
5. **Interacting with the Project**

To interact with the project, follow these steps:

1. Open a new tab in your web browser.
2. Enter the following URL: `http://localhost:8081`

You can log in using the default credentials:

- Username: `user01`
- Password: `password`

Please note that these credentials, along with other user data, can be found in the JSON files located in the `resources/data/` directory of the project.
#####################################################
## Contact

For any inquiries or support, please contact [ndtung723@gmail.com] or visit the project's [GitHub Issues](https://github.com/tungducng/media-editor-java/issues) page.

---

Thank you for using Welcome!
