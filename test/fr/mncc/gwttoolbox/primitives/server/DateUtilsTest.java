package fr.mncc.gwttoolbox.primitives.server;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DateUtilsTest {

  @Test
  @SuppressWarnings("deprecation")
  public void testToDateIso() {
    boolean thrown = false;
    try {
      String str = DateUtils.toDateIso(null);
      assertEquals(str, "1970-01-01");

      str = DateUtils.toDateIso(new Date(12, 12, 12));
      assertEquals(str, "1913-01-12");
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testFromDateIso() {
    boolean thrown = false;
    try {
      Date date = DateUtils.fromDateIso(null);
      assertEquals(date.getYear(), 70);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 1);

      date = DateUtils.fromDateIso("1913-01-12");
      assertEquals(date.getYear(), 13);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 12);
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testToTimeIso() {
    boolean thrown = false;
    try {
      String str = DateUtils.toTimeIso(null);
      assertEquals(str, "01:00:00");

      str = DateUtils.toTimeIso(new Date(12, 12, 12, 12, 12, 12));
      assertEquals(str, "12:12:12");
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testFromTimeIso() {
    boolean thrown = false;
    try {
      Date date = DateUtils.fromTimeIso(null);
      assertEquals(date.getHours(), 1);
      assertEquals(date.getMinutes(), 0);
      assertEquals(date.getSeconds(), 0);

      date = DateUtils.fromTimeIso("12:12:12");
      assertEquals(date.getHours(), 12);
      assertEquals(date.getMinutes(), 12);
      assertEquals(date.getSeconds(), 12);
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testToTimestampIso() {
    boolean thrown = false;
    try {
      String str = DateUtils.toTimestampIso(null);
      assertEquals(str, "1970-01-01 01:00:00");

      str = DateUtils.toTimestampIso(new Date(12, 12, 12, 12, 12, 12));
      assertEquals(str, "1913-01-12 12:12:12");
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  @SuppressWarnings("deprecation")
  public void testFromTimestampIso() {
    boolean thrown = false;
    try {
      Date date = DateUtils.fromTimestampIso("1970-01-01 01:00:00");
      assertEquals(date.getYear(), 70);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 1);
      assertEquals(date.getHours(), 1);
      assertEquals(date.getMinutes(), 0);
      assertEquals(date.getSeconds(), 0);

      date = DateUtils.fromTimestampIso("1913-01-12 12:12:12");
      assertEquals(date.getYear(), 13);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 12);
      assertEquals(date.getHours(), 12);
      assertEquals(date.getMinutes(), 12);
      assertEquals(date.getSeconds(), 12);
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }
}
