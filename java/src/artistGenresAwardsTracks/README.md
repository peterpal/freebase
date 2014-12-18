## Preprocessor.java

Preprocessor class ensures, that all necessary preconditions for running **Service.java** class are met. Freebase data dump is parsed and split to six specific files and subsequently indexed. 

## Service.java

Main class for the provided service. It takes only one input argument, containing desired search string (e.g. "jazz reggae*") and creates folder "results" with files, containing found track names. Files are grouped based on track artist.