package com.example.windsurfaidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Windsurf AI デモアプリケーションのメインクラスです。 */
@SpringBootApplication
public class WindsurfAiDemoApplication {

  /** デフォルトコンストラクタです。 */
  public WindsurfAiDemoApplication() {
    // Springがインスタンス化するためのコンストラクタ
  }

  /**
   * アプリケーションのエントリポイント。
   *
   * @param args コマンドライン引数
   */
  public static void main(final String[] args) {
    SpringApplication.run(WindsurfAiDemoApplication.class, args);
  }
}
