package fr.mncc.gwttoolbox.primitives.client;

import com.google.gwt.junit.client.GWTTestCase;

import java.sql.Time;
import java.util.Date;

public class DateUtilsTest extends GWTTestCase {

  @Override
  public String getModuleName() {
    return "fr.mncc.gwttoolbox.primitives.primitives";
  }

  @SuppressWarnings("deprecation")
  public void testToDateIso() {
    boolean thrown = false;
    try {
      String str = fr.mncc.gwttoolbox.primitives.client.DateUtils.toDateIso(null);
      assertEquals(str, "1970-01-01");

      str = fr.mncc.gwttoolbox.primitives.client.DateUtils.toDateIso(new Date(12, 12, 12));
      assertEquals(str, "1913-01-12");
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @SuppressWarnings("deprecation")
  public void testFromDateIso() {
    boolean thrown = false;
    try {
      Date date = fr.mncc.gwttoolbox.primitives.client.DateUtils.fromDateIso(null);
      assertEquals(date.getYear(), 70);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 1);

      date = fr.mncc.gwttoolbox.primitives.client.DateUtils.fromDateIso("1913-01-12");
      assertEquals(date.getYear(), 13);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 12);
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @SuppressWarnings("deprecation")
  public void testToTimeIso() {
    boolean thrown = false;
    try {
      String str = fr.mncc.gwttoolbox.primitives.client.DateUtils.toTimeIso(null);
      assertEquals(str, "01:00:00");

      str = fr.mncc.gwttoolbox.primitives.client.DateUtils.toTimeIso(new Time((new Date(12, 12, 12, 12, 12, 12)).getTime()));
      assertEquals(str, "12:12:12");
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @SuppressWarnings("deprecation")
  public void testFromTimeIso() {
    boolean thrown = false;
    try {
      Date date = fr.mncc.gwttoolbox.primitives.client.DateUtils.fromTimeIso(null);
      assertEquals(date.getHours(), 1);
      assertEquals(date.getMinutes(), 0);
      assertEquals(date.getSeconds(), 0);

      date = fr.mncc.gwttoolbox.primitives.client.DateUtils.fromTimeIso("12:12:12");
      assertEquals(date.getHours(), 12);
      assertEquals(date.getMinutes(), 12);
      assertEquals(date.getSeconds(), 12);
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @SuppressWarnings("deprecation")
  public void testToTimestampIso() {
    boolean thrown = false;
    try {
      String str = fr.mncc.gwttoolbox.primitives.client.DateUtils.toTimestampIso(null);
      assertEquals(str, "1970-01-01 01:00:00");

      str = fr.mncc.gwttoolbox.primitives.client.DateUtils.toTimestampIso(new Date(12, 12, 12, 12, 12, 12));
      assertEquals(str, "1913-01-12 12:12:12");
    }
    catch (Exception e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @SuppressWarnings("deprecation")
  public void testFromTimestampIso() {
    boolean thrown = false;
    try {
      Date date = fr.mncc.gwttoolbox.primitives.client.DateUtils.fromTimestampIso("1970-01-01 01:00:00");
      assertEquals(date.getYear(), 70);
      assertEquals(date.getMonth(), 0);
      assertEquals(date.getDate(), 1);
      assertEquals(date.getHours(), 1);
      assertEquals(date.getMinutes(), 0);
      assertEquals(date.getSeconds(), 0);

      date = fr.mncc.gwttoolbox.primitives.client.DateUtils.fromTimestampIso("1913-01-12 12:12:12");
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
