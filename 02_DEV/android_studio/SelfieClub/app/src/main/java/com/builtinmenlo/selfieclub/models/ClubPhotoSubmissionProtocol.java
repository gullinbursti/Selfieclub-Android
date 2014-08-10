package com.builtinmenlo.selfieclub.models;

/**
 * Created by Leonardo on 7/23/14.
 */
public interface ClubPhotoSubmissionProtocol {
    /**
     * Called once the photo was submitted into the club
     * @param result True if the photo was submitted
     */
    public void didSubmittedPhotoInClub(Boolean result);

    /**
     * Called if something went wrong with the submission
     * @param message The error message
     */
    public void didFailSubmittingPhotoInClub(String message);
}
