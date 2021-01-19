		package org.campusmarket.app;
		
		import org.campusmarket.db.repositories.ItemsRepository;
		import org.campusmarket.db.repositories.SessionsRepository;
		import org.campusmarket.db.repositories.UsersRepository;
		import org.junit.Before;
		import org.mockito.Mock;
		import org.mockito.MockitoAnnotations;
		
		public abstract class TestServices {
			@Mock
			protected ItemsRepository itemsRepo;
			
			@Mock
			protected UsersRepository UsersRepo;
			
			@Mock
			protected SessionsRepository SessionsRepo;
			
			@Before
			public void init() {
				MockitoAnnotations.initMocks(this);
			}
			
		}
