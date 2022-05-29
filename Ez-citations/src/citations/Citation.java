package citations;

import java.util.*;

public class Citation {

	public ArrayList<String> authorsLastName;
	public ArrayList<String> authorsFirstName;
	public String sourceTitle;
	public String firstContainer;
	public String otherContributors;
	public String version;
	public String numbers;
	public String publisher;
	public String publicationDate;
	public String location;
	public String customCitation;
	public String annotation;

	public Citation(ArrayList<String> aln, ArrayList<String> afn, String srctitle, String firstC, String otherC,
			String ver, String num, String pub, String pubDate, String loc, String cus, String ann) {
		authorsLastName = aln;
		authorsFirstName = afn;
		sourceTitle = srctitle;
		firstContainer = firstC;
		otherContributors = otherC;
		version = ver;
		numbers = num;
		publisher = pub;
		publicationDate = pubDate;
		location = loc;
		customCitation = cus;
		annotation = ann;
	}

	public Citation(String cus, String ann) {
		customCitation = cus;
		annotation = ann;
	}

	public String getCitation() {
		if (customCitation != "") {
			return customCitation;
		}
		String citationTxt = "";
		if (authorsLastName.size() == 0) {
			if (sourceTitle.trim().length() > 0) {
				citationTxt += sourceTitle + ". ";
			}
		} else if (authorsLastName.size() == 1) {
			citationTxt += authorsLastName.get(0) + ", " + authorsFirstName.get(0) + ". ";
			if (sourceTitle.trim().length() > 0) {
				citationTxt += "\""+ sourceTitle + ".\" ";
			}
		} else if (authorsLastName.size() == 2) {
			citationTxt += authorsLastName.get(0) + ", " + authorsFirstName.get(0) + " and " + authorsLastName.get(1) + ", " + authorsFirstName.get(1) + ". ";
			if (sourceTitle.trim().length() > 0) {
				citationTxt += sourceTitle + ". ";
			}
		} else {
			for(int i = 0; i < authorsLastName.size()-1; i++) {
				citationTxt += authorsLastName.get(i) + ", ";
			}
			citationTxt += authorsLastName.get(authorsLastName.size()-1) + ". ";
			if (sourceTitle.trim().length() > 0) {
				citationTxt += "\""+ sourceTitle + ".\" ";
			}

		}
		if (firstContainer.trim().length() > 0) {
			citationTxt += firstContainer + ", ";
		}
		if (otherContributors.trim().length() > 0) {
			citationTxt += otherContributors + ", ";
		}
		if (version.trim().length() > 0) {
			citationTxt += version + ", ";
		}
		if (numbers.trim().length() > 0) {
			citationTxt += numbers + ", ";
		}
		if (publisher.trim().length() > 0) {
			citationTxt += publisher + ", ";
		}
		if (publicationDate.trim().length() > 0) {
			citationTxt += publicationDate + ", ";
		}
		if (location.trim().length() > 0) {
			citationTxt += location;
		}

		return citationTxt;
	}

	public String getAnnotations() {
		return annotation;
	}
	
	public void setAnnotations(String ann) {
		annotation = ann;
	}
}
