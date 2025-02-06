# 🛡️ AuthGuard Authentication System

AuthGuard is a tech demo project with the intend to build a modern authentication system that supports multiple authentication methods, including **social login**, **email token verification**, and **QR code authentication**. It provides a seamless and secure user experience across web and mobile platforms.

---

## 🚀 Features
- 🔐 **Social Login** via Google OAuth  
- ✉️ **Email Token Authentication** for passwordless login with Resend and Spring Security One Time Token
- 📲 **QR Code-Based Authentication** to authenticate web sessions via mobile with TOTP
- 🔄 **JWT-Based Session Management**  

---

## 📌 Authentication Flow

### 1️⃣ Social Login (Google OAuth)
1. User selects **Google login** on the frontend.
2. The frontend redirects to **Google OAuth**.
3. After user permission, Google returns an **access token**.
4. The frontend sends the access token to the backend for verification.
5. The backend validates it and generates a **JWT token**.
6. The frontend stores the JWT for further authenticated requests.

---

### 2️⃣ Email Token Authentication
1. The user requests login via email.
2. The backend generates a **one-time authentication token** and sends it via email.
3. The user clicks on the email link, which contains the token.
4. The frontend sends the token to the backend for validation.
5. If valid, the backend issues a **JWT token**, allowing authenticated access.

---

### 3️⃣ QR Code Authentication (Mobile App → Web)
1. **QR Code Generation (Backend → Frontend)**
   - The backend generates a **QR code** with a unique **session identifier**.
   - The web frontend displays the QR code to the user.

2. **QR Code Scanning (Mobile App)**
   - The user opens the **AuthGuard mobile app** and scans the QR code.
   - The app extracts the **session identifier**.

3. **Mobile Authentication Request (Mobile App → Backend)**
   - The mobile app prompts the user for **confirmation** (biometric, PIN, or button press).
   - Once confirmed, the **session identifier** is sent to the backend.

4. **Backend Verification (Backend)**
   - The backend validates the session identifier.
   - If valid, the backend **authenticates the user's web session**.

5. **Web Authentication Confirmation (Backend → Frontend)**
   - The backend notifies the **web frontend** that authentication is successful.
   - The web app automatically **logs in the user**.

---

## 🔄 Communication Overview
| Component  | Action |
|------------|---------|
| **Frontend → Backend** | Requests JWT tokens, validates email tokens, sends session identifiers. |
| **Backend → Frontend** | Issues JWT tokens, responds to authentication requests, notifies of successful authentication. |
| **Mobile App → Backend** | Sends authentication requests after scanning QR codes. |
| **Email Service** | Sends one-time authentication tokens to users. |

---

## 📦 Tech Stack
- **Frontend:** React / Next.js  
- **Mobile App:** React Native  
- **Backend:** Java (Spring Boot)  
- **Database:** PostgreSQL / MongoDB  
- **Authentication:** JWT, OAuth 2.0, TOTP  

---

## 🎯 Next Steps
- 🔄 **WebSocket Integration** for real-time authentication updates.  
- 🏷️ **Multi-Factor Authentication (MFA)** for extra security.  
- 📌 **Session Expiry & Revocation** for enhanced security controls.  

---

## 📜 License
This project is licensed under the **MIT License**.

---

## 🛠️ Contributing
Feel free to submit **issues** and **pull requests**! Contributions are welcome. 🚀
