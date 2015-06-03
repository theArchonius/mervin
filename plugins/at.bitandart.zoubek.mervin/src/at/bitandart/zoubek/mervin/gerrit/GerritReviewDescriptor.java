package at.bitandart.zoubek.mervin.gerrit;

import java.text.MessageFormat;

import at.bitandart.zoubek.mervin.IReviewDescriptor;

/**
 * Describes an review (change) in a Gerrit repository. The review id is the
 * primary key of the change in Gerrit.
 * 
 * @author Florian Zoubek
 *
 */
public class GerritReviewDescriptor implements IReviewDescriptor {

	/**
	 * format for the title description, first argument is the primary key,
	 * second is the change id
	 */
	private static final String TITLE_FORMAT = "Change {0}: {1}";

	/**
	 * the primary key of the change in Gerrit
	 */
	private int primaryKey;

	/**
	 * the change identifier in Gerrit
	 */
	private String changeId;

	/**
	 * @param primaryKey
	 *            the primary key of the change in Gerrit
	 * @param changeId
	 *            the change identifier in Gerrit
	 */
	public GerritReviewDescriptor(int primaryKey, String changeId) {
		this.primaryKey = primaryKey;
		this.changeId = changeId;
	}

	@Override
	public String getId() {
		return primaryKey + "";
	}

	@Override
	public String getTitle() {
		return MessageFormat.format(TITLE_FORMAT, primaryKey, changeId);
	}

	@Override
	public String getShortDescription() {
		// TODO add a more detailed description here
		return getTitle();
	}

	@Override
	public String getLongDescription() {
		// TODO add a more detailed description here
		return getTitle();
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + primaryKey;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GerritReviewDescriptor other = (GerritReviewDescriptor) obj;
		if (primaryKey != other.primaryKey)
			return false;
		return true;
	}

}