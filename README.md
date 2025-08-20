# 🛠️ Ứng dụng Quản Lý Bảo Trì Thiết Bị

Ứng dụng hỗ trợ người dùng trong việc **quản lý và bảo trì thiết bị** một cách hiệu quả, tự động nhắc nhở khi đến lịch bảo trì, đồng thời cung cấp báo cáo và tính năng giao tiếp trực tiếp qua chat box.

---

## 🚀 Tính năng chính

- 📅 **Theo dõi lịch bảo trì**: Quản lý, cập nhật, và theo dõi lịch bảo trì cho từng thiết bị.  
- 📧 **Thông báo qua email**: Gửi email nhắc nhở người dùng khi đến hạn bảo trì.  
- 🌍 **Hỗ trợ đa ngôn ngữ**: Giao diện đa ngôn ngữ thân thiện.  
- 📊 **Báo cáo sự cố**: Ghi nhận và theo dõi các vấn đề phát sinh từ thiết bị.  
- 💬 **Chat box**: Tích hợp chat để trao đổi nhanh giữa người dùng và bộ phận kỹ thuật.  

---

## 🏗️ Kiến trúc hệ thống

- **Frontend**:  
  - [ReactJS](https://react.dev/)  
  - [Thymeleaf](https://www.thymeleaf.org/) (template engine cho các form server-side)  

- **Backend**:  
  - [Spring MVC](https://spring.io/guides/gs/serving-web-content/)  
    - REST API  
    - Form Binding  

- **Cơ sở dữ liệu**:  
  - [MySQL](https://www.mysql.com/)  

---

## ⚙️ Cài đặt & Chạy thử

### Yêu cầu môi trường
- Node.js >= 18  
- Java 17 hoặc cao hơn  
- Maven >= 3.8  
- MySQL 8  

### Backend
```bash
# clone repo
git clone https://github.com/your-repo/device-maintenance.git
cd device-maintenance/backend

# build & run
mvn spring-boot:run
