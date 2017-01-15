#deliciousparser

Program that parses bulk HTMLs downloaded from [del.icio.us bookmark server](https://del.icio.us/) and outputs them into one clean JSON formatted file. This JSON can be imported into Chrome bookmarks using the chrome extension [import-bookmarks-to-chrome](https://github.com/mikematic/import-bookmarks-to-chrome).

###Packaging

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

### Using

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
      java -jar deliciousparser-1.0-jar-with-dependencies.jar <download directory path> <output directory path>
   ```

   *<download directory path>: The full path to the directory of htmls downloaded from delicious server using wget*

   *<output directory path>: The full path to the directory where you want the json formatted file to be output*

5. Import the JSON data inside the file into Chrome bookmarks using the chrome extension [import-bookmarks-to-chrome](https://github.com/mikematic/import-bookmarks-to-chrome).

### More info
The main purpose I developed this program was to help in bulk importing of my del.icio.us bookmarks into chrome. [del.icio.us](https://del.icio.us/) seems to be currently running out of business and I needed to migrate my bookmarks into another reliable bookmark manager like Chrome. However, they have disabled the feature to export bookmarks and I had more than one thousand bookmarks that were kind of held hostage. I was able to download my bookmarks using the wget tool albeit in html format cluttered with a lot of html tags. [This](https://github.com/mikematic/deliciousparser) Java program parses these htmls into one clean JSON formatted file that can be used by the extension [import-bookmarks-to-chrome](https://github.com/mikematic/import-bookmarks-to-chrome) to import them into Chrome bookmarks.

### Contributions
Stick a fork at it

### License
Apache
