import { Body, Controller, Post } from '@nestjs/common';

@Controller('auth')
export class AuthController {
  @Post('register') // Create the API endpoint
  register(@Body() body: any) {  // Get the data submitted by the user
    return {
      message: 'Đăng ký thành công',
      data: body,
    };
  }
}