package xyz.hellothomas.jedi.core.schedule;

/**
 * Schedule policy
 * @author Jason Song(song_s@ctrip.com)
 */
public interface SchedulePolicy {
    long fail();

    void success();
}
