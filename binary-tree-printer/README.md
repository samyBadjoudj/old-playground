# Binary tree printer

This mini project is web backend (can be use in command line application) to display colored binary tree.  
When I was doing some exercises on BT, wanted to display them but did not find a piece of code read to paste.  

It is composed with these endpoints. 

----
            @GetMapping("printBtFromString")
            public String printBtFromParsable(@RequestParam("content") String content)

            @GetMapping("printBstFromArrayString")
            public String printBstFromArrayString(@RequestParam("content") String content)
----


### Feature
- Build binary tree form this syntax 4(2(3)(1))(6(5)(7(9)))
- Build binary search tree from array unsorted array
- Pretty display on terminal
- Webservices which returns binary tree

### Tech stack

- Spring Boot embedded tomcat

### Screenshot

![console print binary tree](https://raw.githubusercontent.com/samyBadjoudj/binary-tree-printer/master/html_print_bt_console_2.png)

### Client with React,Redux and Sagas 

https://github.com/samyBadjoudj/old-playground/binary-tree-printer-react-client


