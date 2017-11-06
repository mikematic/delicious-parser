# deliciousparser

Program that parses bulk HTMLs downloaded from [del.icio.us bookmark server](https://del.icio.us/) and outputs them into one clean JSON formatted file. This JSON can be imported into Chrome bookmarks using the chrome extension [import-bookmarks-to-chrome](https://github.com/mikematic/import-bookmarks-to-chrome).

Update: Program parses bookmarks exported using del.icio.us api

### Packaging

1. Download or Clone this repository

  ```
  git clone https://github.com/mikematic/deliciousparser.git
  ```

2. Package repository to create your runnable jar

   ```
   mvn clean package
   ```

3. Runnable jar packaged with its dependencies will be located in the target folder inside the home directory of project
   ```
   $[delicious parser HOME]/target/deliciousparser-1.0-jar-with-dependencies.jar
   ```

## Using

### Parsing bookmarks exported using del.icio.us api
1. Login to del.icio.us account and use export feature to export bookmarks as one html

2. Go to the target folder located in the home directory of the project
   ```
   cd $[delicious parser HOME]/target
   ```

3. Parse the exported html by running the jar in the target folder

   ```
      java -jar deliciousparser-1.0-jar-with-dependencies.jar /path/to/exported/html /path/to/directory/for/output

### Parsing bookmarks downloaded directly using wget
1. Download bookmarks from delicious server

   ```
   wget -r https://del.icio.us/<username>/
   ```

2. Go to the target folder located in the home directory of the project
   ```
   cd $[delicious parser HOME]/target
   ```

3. Parse the downloaded htmls by running the jar in the target folder

   ```
      java -jar deliciousparser-1.0-jar-with-dependencies.jar /path/to/downloaded/htmls /path/to/directory/for/output
   ```
5. Import the JSON data inside the file into Chrome bookmarks using the chrome extension [import-bookmarks-to-chrome](https://github.com/mikematic/import-bookmarks-to-chrome).

### Contributions
Stick a fork at it

### License
Apache
