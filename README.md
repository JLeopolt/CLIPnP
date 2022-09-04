# CLIPnP

A command-line interface for UPnP (Universal Plug 'n' Play), made with Java. 
If you've ever been interested in hosting a minecraft server or ever needed to configure port forwarding on your router,
you're in luck! This lightweight Java command-line interface can automatically port-forward for you, with just a single command.

Just run the program, and type "help" for a brief overview of the commands and their functionality.
If you're interested in automating the process, or have a ton of ports you have to type in every time,
or you want the program to save your configuration on restarts, then look no further! With the simple built-in
JSON configuration manager, you can easily export and import configurations from a file.

To save your config on restarts, add a filepath into your command-line argument when launching the program, pointing
to the config file. Just make sure to put the filepath in single quotes! Create new config files, mix them together, 
and save them, all with simple user-friendly commands.

Are you a coder yourself? Working with CLIPnP is a breeze with the specific, transparent JavaDoc!

Jar not launching? Here's a quick-fix:
WINDOWS:
1. Create a new text-file; rename it to *run.bat
2. Open it, then type: 
- java -jar <path to your jar file, and it's filename>.jar
3. Save the file
4. Run it

LINUX:
1. Create a new .sh file
2. Open it, then type:
- #!/bin/sh
- cd <folder with your jar file>
- java -jar <your jar's filename>.jar

Copyright (c) 2022 PyroNeon Software.
Licensed Under GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
