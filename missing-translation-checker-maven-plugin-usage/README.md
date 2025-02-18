# missing-translation-checker-maven-plugin-usage
Example how to use the maven plugin to find missing translation

Look at the pom (https://github.com/samyBadjoudj/old-playground/missing-translation-checker-maven-plugin-usage/blob/master/pom.xml)
to see how it is configured.


I have put the interesting part of the mvn clean install output
```
[WARNING] Can't find/open file /Users/sbadjoudj/tp/missing-translation-checker-maven-plugin-usage/missing-translation-checker-maven-plugin-usage/src/main/resources/MessagesBundle_de_US.properties

[WARNING] [Missing translations for MessagesBundle_fr_FR.properties]
[WARNING] -> added.property

[WARNING] [Missing translations for MessagesBundle_de_DE.properties]
[WARNING] -> french.only.property
          -> added.property

```

Samy Badjoudj
