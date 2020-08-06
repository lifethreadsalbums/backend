package com.poweredbypace.pace.domain.layout;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("spreadcut")
public class FilmStripSpreadCut extends FilmStripItem {

	private static final long serialVersionUID = 3113131930086906752L;

}
