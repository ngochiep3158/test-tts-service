# Sử dụng môi trường Node.js
FROM node:20-alpine

# Tạo thư mục làm việc bên trong container
WORKDIR /app

# Copy 2 file package.json và package-lock.json vào trước
COPY package*.json ./

# Cài đặt các thư viện (dependencies)
RUN npm install

# Copy toàn bộ code vào
COPY . .

# Build code NestJS
RUN npm run build

# Lệnh chạy ứng dụng khi container khởi động
CMD ["npm", "run", "start:prod"]