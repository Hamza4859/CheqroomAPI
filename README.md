---CHEQROOM API - FIRST CALLS ------

This repo is about testing the basic features of the Cheqroom API.


How to setup the environment?
1- Make sure you have Java installed (java --version) (by default on Dana Laptops)
2- Make sure you have git installed (git --version)
3- Make sure you have maven installed (mvn --version)


How to install git?
Download Windows x64 setup at: https://git-scm.com/install/windows
Run .exe. No Admin access is required
Run git --version on terminal to verify it is installed correctly

How to install Maven?
Download apache-maven bin at: https://maven.apache.org/download.cgi
Unzip, copy path
Go to environment variables for your account
Modify PATH variable
Add your path to maven: [Path]\apache-maven-3.9.16\bin
Run mvn --version on terminal to verify it is setup correctly



How to clone repo locally on your end?
Create a github account
Download VS Code and Github extensions
Connect to github on VS Code
Create a folder where you want to clone the repo
Run on terminal: git clone https://github.com/Hamza4859/CheqroomAPI


How to start program?
Open a new terminal from github and run: mvn compile exec:java "-Dexec.mainClass=com.cheqroom.Main"    
Once compiled a first time you can also run it from main.java and click Run on VS Code.