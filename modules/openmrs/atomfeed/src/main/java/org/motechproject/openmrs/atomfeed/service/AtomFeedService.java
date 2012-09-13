package org.motechproject.openmrs.atomfeed.service;

import org.motechproject.MotechException;
import org.motechproject.openmrs.atomfeed.events.EventDataKeys;
import org.motechproject.openmrs.atomfeed.events.EventSubjects;

/**
 * AtomFeedService allow users to fetch an atom feed provided by the OpenMRS Atom
 * Feed module. <br />
 * If the atom feed contains new entries, corresponding events will be raised in
 * the Motech server that correspond to those actions.
 *
 * Clients interested in listening for events should see {@link EventSubjects}
 * for a full description of possible events that may be generated by the Atom
 * Feed. The data associated with each event can be accessed with
 * {@link EventDataKeys}.
 */
public interface AtomFeedService {

    /**
     * Fetches the Atom Feed from the OpenMRS. This will retrieve all entries in
     * the OpenMRS Atom Feed.
     *
     * @throws MotechException
     *             If there was a problem fetching the openmrs atom feed
     */
    void fetchAllOpenMrsChanges();

    /**
     * Fetch changes from the OpenMRS Atom Feed, and retrieve only changes that
     * have occurred since the atom feed was last checked by this service
     */
    void fetchOpenMrsChangesSinceLastUpdate();

    /**
     * Fetch changes from the OpenMRS Atom Feed since <code>sinceDateTime</code>
     * and <code>lastId</code>. If <code>sinceDateTime</code> is null, this is
     * equivalent to calling {@link #fetchAllOpenMrsChanges()}
     *
     * @param sinceDateTime
     *            A date time formatted as: 2012-07-09T13:55:35
     * @param lastId
     *            The id of an entry to further filtering on. The id is only
     *            relevant it there is an entry that was updated at
     *            <code>sinceDateTime</code>, otherwise this parameter is
     *            ignored
     */
    void fetchOpenMrsChangesSince(String sinceDateTime, String lastId);
}