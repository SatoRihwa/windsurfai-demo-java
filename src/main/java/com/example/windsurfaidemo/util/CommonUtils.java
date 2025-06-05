package com.example.windsurfaidemo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** アプリケーション全体で使用する汎用的なユーティリティメソッドを提供するクラス */
public class CommonUtils {

  private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

  /**
   * 現在の日時を指定されたフォーマットの文字列で返します
   *
   * @return フォーマットされた現在日時の文字列
   */
  public static String getCurrentFormattedDateTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
  }

  /**
   * ユニークなIDを生成します
   *
   * @return 生成されたユニークID
   */
  public static String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * 文字列がnullまたは空文字列かどうかを判定します
   *
   * @param str チェックする文字列
   * @return nullまたは空文字列の場合はtrue、それ以外はfalse
   */
  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * 文字列の最初の文字を大文字に変換します
   *
   * @param str 変換する文字列
   * @return 変換後の文字列
   */
  public static String capitalize(String str) {
    if (isNullOrEmpty(str)) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  /**
   * 文字列が指定された長さを超えている場合に切り詰めます
   *
   * @param str 対象の文字列
   * @param maxLength 最大長
   * @return 切り詰められた文字列
   */
  public static String truncate(String str, int maxLength) {
    if (str == null) {
      return null;
    }
    if (str.length() <= maxLength) {
      return str;
    }
    return str.substring(0, maxLength) + "...";
  }
}
