import { AppController } from './app.controller';
import { AppService } from './app.service';

describe('AppController', () => {
  let appController: AppController;
  let appService: AppService;
  let fetchSpy: jest.SpyInstance;

  beforeEach(() => {
    fetchSpy = jest.spyOn(globalThis, 'fetch');
    appService = new AppService();
    appController = new AppController(appService);
  });

  afterEach(() => {
    fetchSpy.mockRestore();
  });

  describe('root', () => {
    it('should return "Hello World!"', () => {
      expect(appController.getHello()).toBe('Hello World!');
    });
  });

  describe('convertTextToSpeech', () => {
    it('forwards the request to the TTS service and returns its response', async () => {
      const payload = { text: 'Xin chào', lang: 'vi' };
      const responseBody = {
        success: true,
        originalText: 'Xin chào',
        language: 'vi',
        audioUrl: 'https://example.com/audio',
      };
      fetchSpy.mockResolvedValue(
        new Response(JSON.stringify(responseBody), { status: 200 }),
      );

      await expect(
        appController.convertTextToSpeech(payload),
      ).resolves.toEqual(responseBody);
      expect(fetchSpy).toHaveBeenCalledWith(
        'http://localhost:3003/api/tts/convert',
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        },
      );
    });

    it('preserves errors returned by the TTS service', async () => {
      const responseBody = {
        success: false,
        message: 'Vui lòng cung cấp văn bản (text)',
      };
      fetchSpy.mockResolvedValue(
        new Response(JSON.stringify(responseBody), { status: 400 }),
      );

      await expect(
        appService.convertTextToSpeech({ text: '' }),
      ).rejects.toMatchObject({
        status: 400,
        response: responseBody,
      });
    });

    it('reports TTS service connection failures as unavailable', async () => {
      fetchSpy.mockRejectedValue(new Error('connection refused'));

      await expect(
        appService.convertTextToSpeech({ text: 'Xin chào' }),
      ).rejects.toMatchObject({ status: 503 });
    });
  });
});
