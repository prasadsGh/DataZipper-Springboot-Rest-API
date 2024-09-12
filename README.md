<!-- ABOUT THE PROJECT -->
## About The Project

* Developed REST APIs for encryption and decryption using Huffman Encoding.
* Achieved an approximate 50% reduction in file size after compression. 💾✨
* More overview about Huffman Encoding : https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/ 


🛠️ Key Highlights:
* File Handling: Implemented file handling mechanism for file upload and file creation and writing decrypted text to the created file.

* REST APIs: Implemented two APIs : one for encryption and second one for decryption. 

* Data Structures: Used Data structures for implementing Huffman algorithm .

This project has been a great experience in applying the Huffman encoding algorithm within a Spring Boot application.
I’m enthusiastic about collaborating with other developers on innovative projects and exploring new ideas together!

🔗 If you’re interested in collaborating or discussing exciting new projects, let’s connect!

<!-- GETTING STARTED -->
## Getting Started


<!-- Video Demostration -->
Youtube Video  Demonstration:
<br>
[![Watch the video](https://img.youtube.com/vi/canBUAtUq8Q/0.jpg)](https://www.youtube.com/watch?v=canBUAtUq8Q&t=1s)


<!-- GETTING STARTED -->
## Getting Started


### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* Java (Spring Boot)
* Maven 
* Java SDK
* IntelliJ
* Postman


<!-- USAGE EXAMPLES -->
## Usage
* Input File Size
<br>
  ![input file image](/readme images/input File.png)

Start Spring Boot Application in IntelliJ (or Any other your favourite IDE)

* Send a text file through postman to following api on your local machine to encrypt it.
<br>
http://localhost:8060/api/file/upload
<br>
  ![sending input file through postman.png](/readme images/sending input file through postman.png)
<br>
after successful processing, the file with named as [output] will be created at your project folder, this [output] file is the encrypted/compressed file.
* Encrypted text file
 <br>
  ![encypted text file.png](/readme images/encypted text file.png)

* Send an encrypted text file through postman to following API on your local machine to decrypt it
<br>
http://localhost:8060/api/file/download
<br>
  ![sending encrypte file.png](/readme images/sending encrypte file.png)
<br>

after successfull processing, the file named with [decrypyted.txt] will be created at your project folder along with all the  text inside it as the original one.
as huffman encoding is lossless data compression technique, you will be able to find exact same data in [ decrypted.txt] as the original text file.
* Decrypted text file 
<br>
  ![decrypted text file.png](/readme images/decrypted text file.png)
<br>
* Original File Size
<br>
  ![original input file size.png](/readme images/original input file size.png)
* Encrypted File Size & Decrypted File Size
<br>
encrypted file name-output
decrypted file name - decrypted
<br>
  ![encypted file size and decrypted file size.png](/readme images/encypted file size and decrypted file size.png)

Note: Give key name as "file" while sending file through postman to the application






<!-- CONTACT -->
## Contact

X.com[Twitter] - [@Twitter](https://twitter.com/prasad_2301) 

<!--Project Link: [https://github.com/your_username/repo_name](https://github.com/your_username/repo_name)-->

