# Reactive NoSQL In-Memory Database

This repository contains an implementation of a mini-in-memory NoSQL database with reactive features. The database supports storing unstructured data using key-value pairs, similar to a hashtable.

## Features

- Support for various data types: Strings, Numbers, Arrays, and Objects.
- Basic operations: add data, retrieve data by key, replace data at a key, and remove key-value pairs.
- Transaction support: create independent transactions, perform operations within a transaction, and commit or abort the transaction.
- Persistence: save the database state to files for persistence, and recover the state from saved files.
- Reactive programming: obtain cursor objects to track values and receive notifications on value changes.
- Memento Design Pattern: ensures strong object state by providing the ability to save and restore the state of the database.
- Command Design Pattern: saves executed commands to allow for recovery in case of failure and re-execution on the retrieved object.

## Design Patterns

### Memento Design Pattern

The Memento design pattern is used in the Reactive NoSQL database to maintain a strong state of the object. The database class implements a Memento class that encapsulates the state of the database. This allows the database to save its state at any point in time and restore it later if needed. The Memento pattern ensures that the database can roll back to a previous state, providing a way to revert changes or recover from errors.

### Command Design Pattern

The Command design pattern is used to save commands executed on the database. Each command is encapsulated within a command object, which can be stored and executed later. In the Reactive NoSQL database, the commands executed on the database are saved to a file called "commands.txt". If a failure occurs, the database can recover the state by re-executing the saved commands in the correct order. The Command pattern enables the database to recover from errors and maintain data consistency.
