# AuthGuard

A lightweight, secure, and customizable two-factor authentication (2FA) system built in **Java 21**, inspired by popular authenticators. This project implements **TOTP (Time-based One-Time Password)** to enhance account security.

> **Note**: This project is intended for **learning and practice purposes**. While it follows best practices for security, it may not be production-ready without further testing and validation.

## Features

- **Time-based One-Time Passwords (TOTP)** generation and validation.
- **QR Code Generation** for easy device linking.
- Secure storage of shared secrets.
- Backup codes for account recovery.
- Highly customizable and extendable for different use cases.

## Requirements

- Java 21 or higher
- Maven (or Gradle for dependency management)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/AuthenticatorGuard.git
cd AuthenticatorGuard
