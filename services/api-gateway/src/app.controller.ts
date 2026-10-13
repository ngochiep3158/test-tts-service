import { Body, Controller, Get, Post } from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getHello(): string {
    return this.appService.getHello();
  }

  @Post('api/tts/convert')
  convertTextToSpeech(
    @Body() payload: { text: string; lang?: string },
  ): Promise<unknown> {
    return this.appService.convertTextToSpeech(payload);
  }
}
