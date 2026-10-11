import { Body, Controller, Post } from '@nestjs/common';

@Controller('auth')
export class AuthController {
  @Post('register') // tạo API
  register(@Body() body: any) {  // Lấy dữ liệu người dùng gửi
    return {
      message: 'Đăng ký thành công',
      data: body,
    };
  }
}