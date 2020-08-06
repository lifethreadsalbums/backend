package com.poweredbypace.pace.domain.layout;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("pagecut")
public class FilmStripPageCut extends FilmStripItem {

	private static final long serialVersionUID = -6777824648592741406L;

	
}
