package org.motechproject.appointmentreminder.model;

import java.util.Collections;
import java.util.Set;

import javax.management.DescriptorKey;

import org.ektorp.docref.DocumentReferences;
import org.ektorp.docref.FetchType;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechAuditableDataObject;

public class Patient extends MotechAuditableDataObject {

	private static final long serialVersionUID = -4678392647206490010L;
	@TypeDiscriminator
	private String clinicPatientId;
	private String gender;
	private Clinic clinic;
	private Doctor doctor;
	private String phoneNumber;
	private Preferences preferences;
    @DocumentReferences(fetch = FetchType.LAZY, descendingSortOrder = true, orderBy = "visitDate", backReference = "patientId")
    private Set<Visit> visits;

    @DocumentReferences(fetch = FetchType.LAZY, descendingSortOrder = true, orderBy = "windowStartDate", backReference = "patientId")
    private Set<Appointment> appointments;

	
	/**
	 * @return the clinicPatientId
	 */
	public String getClinicPatientId() {
		return clinicPatientId;
	}
	/**
	 * @param clinicPatientId the clinicPatientId to set
	 */
	public void setClinicPatientId(String clinicPatientId) {
		this.clinicPatientId = clinicPatientId;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the clinic
	 */
	public Clinic getClinic() {
		return clinic;
	}
	/**
	 * @param clinic the clinic to set
	 */
	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}
	/**
	 * @return the doctor
	 */
	public Doctor getDoctor() {
		return doctor;
	}
	/**
	 * @param doctor the doctor to set
	 */
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
	/**
	 * @return the preferences
	 */
	public Preferences getPreferences() {
		return preferences;
	}
	/**
	 * @param preferences the preferences to set
	 */
	public void setPreferences(Preferences preferences) {
		this.preferences = preferences;
	}
	
    public Set<Visit> getVisits() {
        return visits == null ? Collections.<Visit>emptySet(): visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit) {
		this.visits.add(visit);
	}

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments  == null ? Collections.<Appointment>emptySet(): appointments;
    }
    
    public void addAppointment(Appointment appointment) {
    	this.appointments.add(appointment);
    }

	
	
    @Override
    public String toString() {
        return "id = " + this.getId() + ", clinic patient id = " + clinicPatientId + ", gender = " + this.gender + ", phone number = " + phoneNumber + "preferences = {" + ((this.preferences != null) ? this.preferences.toString() : "null") + ", clinic = {" + ((this.clinic != null) ? this.clinic.toString() : "null") + "}, doctor = {" + ((this.doctor != null) ? doctor.toString() : "null") + "}"; 
    }
    
    @Override
    public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    
	    Patient p = (Patient) o;
	    if (this.getId() != null ? !this.getId().equals(p.getId()) : p.getId() != null) return false;
	    if (this.clinicPatientId != null ? !this.clinicPatientId.equals(p.getClinicPatientId()) : p.getClinicPatientId() != null) return false;
	    if (this.clinic != null ? !this.clinic.equals(p.getClinic()) : p.getClinic() != null) return false;
	    if (this.doctor != null ? !this.doctor.equals(p.getDoctor()) : p.getDoctor() != null) return false;
	    if (this.phoneNumber != null ? !this.phoneNumber.equals(p.getPhoneNumber()) : p.getPhoneNumber() != null) return false;
	    
        return true;
    }

    @Override
    public int hashCode() {
	    int result = this.getId() != null ? this.getId().hashCode() : 0;
	    result = 31 * result + (this.clinicPatientId != null ? this.clinicPatientId.hashCode() : 0);
	    result = 31 * result + (this.clinic != null ? this.clinic.hashCode() : 0);
	    result = 31 * result + (this.gender != null ? this.gender.hashCode() : 0);
	    result = 31 * result + (this.doctor != null ? this.doctor.hashCode() : 0);
	    result = 31 * result + (this.phoneNumber != null ? this.phoneNumber.hashCode() : 0);
	    return result;
    }
	
}