import {
  HttpException,
  Injectable,
  ServiceUnavailableException,
} from '@nestjs/common';

@Injectable()
export class AppService {
  private readonly ttsServiceUrl =
    process.env.TTS_SERVICE_URL ?? 'http://localhost:3003';

  getHello(): string {
    return 'Hello World!';
  }

  async convertTextToSpeech(payload: {
    text: string;
    lang?: string;
  }): Promise<unknown> {
    let response: Response;

    try {
      response = await fetch(
        `${this.ttsServiceUrl.replace(/\/$/, '')}/api/tts/convert`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        },
      );
    } catch (error) {
      throw new ServiceUnavailableException('Unable to reach TTS service', {
        cause: error,
      });
    }

    const responseBody: unknown = await response.json();
    if (!response.ok) {
      if (responseBody === null || typeof responseBody !== 'object') {
        throw new HttpException(String(responseBody), response.status);
      }
      throw new HttpException(responseBody, response.status);
    }

    return responseBody;
  }
}
