#  ỨNG DỤNG QUẢN LÝ TIẾN ĐỘ CÔNG VIỆC

## ️ Giới thiệu

Ứng dụng **Quản lý tiến độ công việc** là một hệ thống giúp **người dùng, nhóm hoặc tổ chức** có thể **tạo, theo dõi và quản lý các dự án cũng như nhiệm vụ (task)** của mình một cách hiệu quả.

Hệ thống hỗ trợ **phân công công việc, gán nhãn, thiết lập thời hạn và theo dõi tiến độ** cho từng nhiệm vụ, đồng thời cho phép **nhiều người dùng cùng tham gia trong một dự án.**

---

##  Các tính năng chính

###  Người dùng (Users)
**Chức năng:**
- Đăng ký tài khoản mới.
- Đăng nhập vào hệ thống.
- Cập nhật thông tin cá nhân (họ tên, email, mật khẩu, v.v.).
- Tạo và quản lý các công việc của bản thân hoặc nhóm.

**Thông tin lưu trữ:**
- Họ tên
- Email
- Mật khẩu
- Ngày tạo tài khoản

---

###  Nhóm làm việc (Teams)
**Chức năng:**
- Tạo nhóm mới.
- Mời thành viên tham gia nhóm.
- Quản lý vai trò (Role) của từng thành viên.

**Thông tin lưu trữ:**
- Tên nhóm
- Ngày tạo
- Danh sách thành viên

---

###  Dự án (Projects)
**Chức năng:**
- Tạo mới, cập nhật, xóa và xem thông tin dự án.
- Phân công người thực hiện (thành viên nhóm).
- Theo dõi tiến độ của từng dự án.

**Thông tin lưu trữ:**
- Tên dự án
- Mô tả chi tiết
- Nhóm thực hiện
- Ngày tạo

---

###  Nhiệm vụ (Tasks)
**Chức năng:**
- Tạo, cập nhật, xóa hoặc xem chi tiết nhiệm vụ.
- Gán nhiệm vụ cho thành viên trong dự án.
- Thiết lập trạng thái (đang làm, hoàn thành, v.v.).
- Thiết lập mức độ ưu tiên và hạn chót (deadline).
- Gắn nhãn (Label) để phân loại công việc.

**Thông tin lưu trữ:**
- Tiêu đề
- Mô tả
- Trạng thái
- Mức độ ưu tiên
- Thời hạn hoàn thành
- Người tạo / người được giao

---

### Nhãn (Labels)
**Chức năng:**
- Tạo nhãn để phân loại công việc.
- Gắn nhiều nhãn cho một nhiệm vụ.

**Thông tin lưu trữ:**
- Tên nhãn
- Màu sắc
- Nhóm sở hữu

---

###  Bình luận (Comments)
**Chức năng:**
- Cho phép người dùng trao đổi, thảo luận trong từng nhiệm vụ.
- Hiển thị thời gian và người bình luận.

**Thông tin lưu trữ:**
- Nội dung bình luận
- Người tạo bình luận
- Thời gian tạo

---

###  Danh sách công việc con (Checklists)
**Chức năng:**
- Thêm các công việc con trong mỗi nhiệm vụ.
- Đánh dấu hoàn thành từng mục nhỏ.

**Thông tin lưu trữ:**
- Tiêu đề danh sách
- Nội dung từng mục
- Trạng thái hoàn thành (đã làm / chưa làm)

---

### Phân quyền (Roles & Permissions)
**Chức năng:**
- Quản lý vai trò của người dùng trong nhóm hoặc dự án.
- Gán quyền cụ thể (xem, tạo, sửa, xóa, v.v.) cho từng vai trò.

**Thông tin lưu trữ:**
- Tên vai trò
- Danh sách quyền được phép thực hiện

---


### Công nghệ sử dụng
- Ngôn ngữ Java (JDK 8+)
- UI : Java Swing
- Kiến trúc: Model-View-Controller (MVC)
- Cơ sở dữ liệu : MySQL

---
### Cấu trúc dự án 
Dự án được tổ chức theo mô hình MVC với JavaSwing:
- Model: Chứa các lớp đại diện cho dữ liệu của ứng dụng
- View: Giao diện ngươời dùng desktop
- Controller : Xử lý các yêu cầu từ người dùng, tương tác với Model
- DAO : Data Access Objects - Tương tác với database


Dự án được xây dựng dựa trên mô hình hướng đối tượng với các lớp chính 

<div align="center">
<img align="center" style="width: 40%; height: auto;" src="./docs/ClassDiagram.png">
</div>
