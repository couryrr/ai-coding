# Agent Instructions

This file contains guidelines for AI agents operating in this repository.

## Commands
No standard build/test/lint commands have been established yet. Please ask the user for specific commands when needed.

## Code Style Guidelines

### Imports
- Group imports by type (built-in, external, internal)
- Use absolute imports from project root
- No unused imports

### Formatting
- Use consistent indentation (2 spaces recommended)
- Max line length: 100 characters
- Add trailing commas in multi-line lists/objects

### Types
- Use explicit type annotations for function parameters and returns
- Avoid `any` type - use proper types or generics
- Define interfaces for complex objects

### Error Handling
- Use try/catch blocks for error-prone operations 
- Provide meaningful error messages
- Consider logging errors for debugging

### Naming
- Use camelCase for variables and functions
- Use PascalCase for classes and interfaces
- Use descriptive names that indicate purpose