package com.example.SpringSecurity.model;

public record LoginRequestDTO(String username, String password) {

    // record :
    // Final fields: The fields of a record are final by default and cannot be changed after being set at the time of construction.
    // Immutable: record objects are immutable; That is, after creating the object, its values cannot be changed.
    // Constructor, Getter, equals, hashCode, toString: Java automatically creates these methods for record.
    // Code Simplification: record can be used to define classes that are just for data transfer, without having to write extra code.
    // Why record?
    // Reducing code complexity: Instead of writing several lines of code to define fields, constructors, and standard methods, using record can automatically summarize all of this in one line.
    // Immutability: This feature is ideal for building simple classes that are only used to transfer data, preventing unwanted data changes.
    // records cannot implement inheritance; That is, they cannot inherit from another class, but they can implement an interface.

}
