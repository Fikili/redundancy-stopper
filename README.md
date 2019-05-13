# About
Redundancy stopper is Tieto Project for Alfresco Hackathon 2019

# Use case
Quite often the same files are distributed on several place in Alfresco repository. Let's imagine you need to update that file. Will you do that on all places? It's annoying and it can be very error prone especially if files have different filenames.

# Our idea
Custom property is used for storing "size;MD5 hash". Then CMIS query is used to check if there are files with the same combination of size and hash. If there is a match, assoctions are created and Share UI will inform you about that using an indicator.  

# Configuration
Create folder and apply redundancyActionExecuter action with "Run in background". Then just upload same files and see if they are marked with indicator.

# Demo
[![Alfresco Hackathon project](http://img.youtube.com/vi/lSNHPKSDi1w/0.jpg)](http://www.youtube.com/watch?v=lSNHPKSDi1w "Redundancy Stopper")
