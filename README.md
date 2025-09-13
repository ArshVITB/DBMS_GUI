# DBMS GUI Application

A simple Java Swing application for interacting with MySQL databases.

## Features

- **SQL Query Execution**: Enter and execute SQL queries in the text area.
- **Result Filtering**: Filter the query results by entering a search term.
- **Auto-resize Columns**: Columns automatically resize to fit content after each query.
- **Larger, Modern UI**: All text and table content use a larger, easy-to-read font.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL database server running on `localhost:3306`
- MySQL username: `root`, password: `abcd1234` (default, can be changed in code)

## How to Run

1. Make sure your MySQL server is running on `localhost:3306`.
2. Double-click `run_app.bat` in the project directory, or run the following command in the terminal:
   ```cmd
   java -cp .;mysql-connector-java-8.0.27.jar;poi-5.2.3.jar;poi-ooxml-5.2.3.jar src/main/java/DBMSGuiApp.java
   ```
3. The application window will open.
4. Enter your SQL query in the text area and click **Execute Query**.
5. To filter results, enter a search term in the filter field and click **Filter**.

## Project Structure

- `src/main/java/DBMSGuiApp.java` - Main application with GUI and database connectivity.
- `run_app.bat` - Batch file to run the application.
- `pom.xml` - Maven configuration (optional, for IDE integration).
- `mysql-connector-java-8.0.27.jar` - MySQL JDBC driver.
- `poi-5.2.3.jar`, `poi-ooxml-5.2.3.jar` - (Not used in current code, can be removed if not needed).

## Notes

- The filter functionality is client-side and works on the currently displayed results.
- For security, avoid using this application with production databases directly; use appropriate user permissions.
- The application uses default MySQL credentials. Update them in the code if needed.


