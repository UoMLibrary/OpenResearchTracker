package uk.ac.man.library.openresearchtracker.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.man.library.openresearchtracker.entities.Publication;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService{
	
	@Autowired
	private PublicationRepository publicationRepo;

	@Override
	public void save(Publication publication) {
		publicationRepo.save(publication);		
	}

	@Override
	public Page<Publication> publicationFilter(String title, String pureStatus,
			String complianceStatus, String oa_route, 
			String funder_Wellcome, 
			String funder_CRUK, 
			String funder_BHF, 
			String funder_NIHR, 
			String funder_ERC, 
			String funder_UKRI,String facultyName, String schoolName, Pageable pageable) {
		
		return publicationRepo.publicationFilter(title, pureStatus,
				complianceStatus, oa_route, 
				funder_Wellcome, 
				funder_CRUK, 
				funder_BHF, 
				funder_NIHR, 
				funder_ERC, 
				funder_UKRI,facultyName, schoolName, pageable);
	}
	
	@Override
	public List<Publication> publicationFilter(String title, String pureStatus,
			String complianceStatus, String oa_route, 
			String funder_Wellcome, 
			String funder_CRUK, 
			String funder_BHF, 
			String funder_NIHR, 
			String funder_ERC, 
			String funder_UKRI,String facultyName, String schoolName) {
		
		return publicationRepo.publicationFilter(title, pureStatus,
				complianceStatus, oa_route, 
				funder_Wellcome, 
				funder_CRUK, 
				funder_BHF, 
				funder_NIHR, 
				funder_ERC, 
				funder_UKRI,facultyName, schoolName);
	}

	@Override
	public void deleteAll() {
		publicationRepo.deleteAll();
		
	}

	@Override
	public Optional<Publication> findByPureId(String pureId) {
		return publicationRepo.findById(pureId);
	}
}
