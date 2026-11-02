package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Limelight 3A wrapper. This is the only class that talks to the camera.
 *
 * Typical use:
 *   vision.start();            // once, at init
 *   vision.useAprilTags();     // or vision.useColor()
 *   ...each loop...
 *   vision.update();           // fetch the newest result
 *   if (vision.hasTarget()) { double tx = vision.getTx(); ... }
 *
 * If the Limelight is not in the Robot Configuration, everything here is a safe no-op
 * (isAvailable() returns false and hasTarget() is always false).
 */
public class Vision {
    private final Limelight3A limelight; // null if not configured
    private LLResult latest;
    private int activePipeline = Config.PIPELINE_APRILTAG;

    public Vision(HardwareMap hardwareMap) {
        limelight = hardwareMap.tryGet(Limelight3A.class, Config.LIMELIGHT_NAME);
        if (limelight != null) {
            limelight.setPollRateHz(Config.LIMELIGHT_POLL_RATE_HZ);
            limelight.pipelineSwitch(activePipeline);
        }
    }

    /** True if the Limelight was found in the Robot Configuration. */
    public boolean isAvailable() {
        return limelight != null;
    }

    /** Start polling the camera. Call once at init (results are always invalid before this). */
    public void start() {
        if (limelight != null) {
            limelight.start();
        }
    }

    public void stop() {
        if (limelight != null) {
            limelight.stop();
        }
    }

    // ---------------- Pipeline selection ----------------

    public void useAprilTags() {
        switchPipeline(Config.PIPELINE_APRILTAG);
    }

    public void useColor() {
        switchPipeline(Config.PIPELINE_COLOR);
    }

    private void switchPipeline(int pipeline) {
        activePipeline = pipeline;
        latest = null; // drop results that came from the previous pipeline
        if (limelight != null) {
            limelight.pipelineSwitch(pipeline);
        }
    }

    public int getActivePipeline() {
        return activePipeline;
    }

    // ---------------- Reading results ----------------

    /** Fetch the newest result. Call once per loop. */
    public void update() {
        if (limelight != null) {
            latest = limelight.getLatestResult();
        }
    }

    /** True if the latest result is valid, fresh, and from the pipeline we asked for. */
    public boolean hasTarget() {
        return latest != null
                && latest.isValid()
                && latest.getStaleness() < Config.VISION_MAX_STALENESS_MS
                && latest.getPipelineIndex() == activePipeline;
    }

    /** Horizontal offset of the main target from the crosshair, in degrees. 0 if no target (check hasTarget() first). */
    public double getTx() {
        return hasTarget() ? latest.getTx() : 0;
    }

    /** Vertical offset of the main target from the crosshair, in degrees. 0 if no target. */
    public double getTy() {
        return hasTarget() ? latest.getTy() : 0;
    }

    /** Size of the main target as a percent of the image (bigger = closer). 0 if no target. */
    public double getArea() {
        return hasTarget() ? latest.getTa() : 0;
    }

    // ---------------- AprilTag helpers (use with the AprilTag pipeline) ----------------

    /** True if the AprilTag with this id is currently visible. */
    public boolean seesTag(int id) {
        return findTag(id) != null;
    }

    /** Horizontal offset of a specific AprilTag in degrees, or 0 if it isn't visible. */
    public double getTagTx(int id) {
        LLResultTypes.FiducialResult tag = findTag(id);
        return tag != null ? tag.getTargetXDegrees() : 0;
    }

    private LLResultTypes.FiducialResult findTag(int id) {
        if (!hasTarget()) {
            return null;
        }
        for (LLResultTypes.FiducialResult tag : latest.getFiducialResults()) {
            if (tag.getFiducialId() == id) {
                return tag;
            }
        }
        return null;
    }
}
