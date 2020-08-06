package com.poweredbypace.pace.controller;


//@Controller
//@SessionAttributes({"testForm"})
public class DummyController {

	/*
	private final Log log = LogFactory.getLog(DummyController.class);
	
	@Autowired
	private ProductOptionService productOptionService;
	
	@Autowired
	private TProductOptionTypeService productOptionTypeService;
	
	@Autowired
	private PrototypeProductRepository prototypeProductService;
	
	@Autowired
	private ProductManagerImpl productManager;
	
	@Autowired
	private PrototypeProductOptionValueService prototypeProductOptionValueService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ShippingManager shippingManager;
	
	@Autowired
	private TCountryService countryService;
	
	@Autowired
	private ShipmentRepository shipmentService;
	
	@RequestMapping(value = "/test.html", method = RequestMethod.GET)
	public ModelAndView dummy(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("product-test");
		Product product = productManager.createProductFromProtytype(1l);
		//log.debug(product.getProductOptions().size());
		mav.addObject("product", product);
		
//		productService.save(product);
		return mav; 
	}
	
	@ModelAttribute(value = "testForm")
	public ShipmentTestForm getTestForm() {
		return new ShipmentTestForm();
	}
	
	@RequestMapping(value = "/rateTest.html", method = RequestMethod.GET)
	public ModelAndView rateTest(@ModelAttribute(value = "testForm") ShipmentTestForm testForm, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("rateTest");
		testForm.setCountries(countryService.getAll());
		testForm.getBillingAddress().setCompanyName("Powered by PACE Inc.");
		testForm.getBillingAddress().setAddressLine1("2 – 3610 Bonneville Place");
		testForm.getBillingAddress().setCountry("CA");
		testForm.getBillingAddress().setState("BC");
		testForm.getBillingAddress().setCity("Burnaby");
		testForm.getBillingAddress().setZipCode("V3N4T7");

//		testForm.getShippingAddress().setCompanyName("Toronto Zoo");
//		testForm.getShippingAddress().setAddressLine1("2000 Meadowvale Rd");
		testForm.getShippingAddress().setCountry("CA");
//		testForm.getShippingAddress().setState("ON");
//		testForm.getShippingAddress().setCity("Toronto");
//		testForm.getShippingAddress().setZipCode("M1B5K7");

		testForm.getDropshippingAddress().setCountry("CA");
		testForm.setBillingAddressStates(countryService.findByIsoCountryCode(testForm.getBillingAddress().getCountry()).getStates());
		testForm.setShippingAddressStates(countryService.findByIsoCountryCode(testForm.getShippingAddress().getCountry()).getStates());
		testForm.setDropshippingAddressStates(countryService.findByIsoCountryCode(testForm.getDropshippingAddress().getCountry()).getStates());
		return mav;
	}
	
	@RequestMapping(value = "/rateTest.html", method = RequestMethod.POST, params = "rate")
	public ModelAndView rateTestDo(@ModelAttribute(value = "testForm") ShipmentTestForm testForm, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView("rateTest");
		testForm.setCountries(countryService.getAll());
		mav.addObject("testForm", testForm);

		Order order = convertTestFormToOrder(testForm);
		for(ShippingProvider shippingProvider : shippingManager.getAvailableShippingProviders()) {
			List<RateShippingResponse> rateResponses = shippingManager.rateShipment(order, shippingProvider, testForm.getShippingPackages());
			testForm.getRates().put(shippingProvider, rateResponses);
		}
		return mav;
	}

	@RequestMapping(value = "/rateTest.html", method = RequestMethod.POST, params = "storeShipment")
	public ModelAndView storeShipmentDo(@ModelAttribute(value = "testForm") ShipmentTestForm testForm, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView(new RedirectView("/listShipments.html", true));
		testForm.setCountries(countryService.getAll());
		
		List<RateShippingResponse> rateShippingResponses = null;
		for(ShippingProvider provider : testForm.getRates().keySet()) {
			if(testForm.getShippingProviderId().equals(provider.getProviderId())) {
				rateShippingResponses = testForm.getRates().get(provider);
				break;
			}
		}
		
		
		List<Shipment> shipmentsToStore = new ArrayList<Shipment>();
		for(RateShippingResponse rateShippingResponse : rateShippingResponses) {
			for(RateShippingResponseEntry entry : rateShippingResponse.getEntries()) {
				ShippingOption shippingOption = entry.getShippingOption();
				if(testForm.getShippingOptionId().equals(shippingOption.getCode())) {
					Shipment shipment = rateShippingResponse.getShipment();
					shipment.setShippingOption(shippingOption);
					shipmentsToStore.add(shipment);
				}
			}
		}
		shippingManager.storeShipment(shipmentsToStore);
		
		return mav;
	}

	@RequestMapping(value = "/listShipments.html", method = RequestMethod.GET)
	public ModelAndView listShipments(@ModelAttribute(value = "shipmentListForm") ShipmentListForm shipmentListForm, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView("shipmentList");
		shipmentListForm.setShipments(shipmentService.findAll());
		mav.addObject("shipmentListForm", shipmentListForm);
		Set<String> foundShippingProviderIds = new HashSet<String>();
		for(Shipment shipment : shipmentListForm.getShipments()) {
			foundShippingProviderIds.add(shipment.getShippingProviderId());
		}
		mav.addObject("foundShippingProviderIds", foundShippingProviderIds);
		return mav;
	}

	@RequestMapping(value = "/listShipments.html", method = RequestMethod.POST, params = "removeAll")
	public ModelAndView removeAllShipments(@ModelAttribute(value = "shipmentListForm") ShipmentListForm shipmentListForm, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView(new RedirectView("/listShipments.html", true));
		for(Shipment shipment : shipmentService.findAll()) {
			shipmentService.delete(shipment);
		}
		return mav;
	}

	@RequestMapping(value = "/rateTest.html", method = RequestMethod.POST)
	public ModelAndView updateFieldsDo(@ModelAttribute(value = "testForm") ShipmentTestForm testForm, HttpServletRequest request, HttpServletResponse response, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView("rateTest");
		testForm.setCountries(countryService.getAll());
		testForm.setBillingAddressStates(countryService.findByIsoCountryCode(testForm.getBillingAddress().getCountry()).getStates());
		testForm.setShippingAddressStates(countryService.findByIsoCountryCode(testForm.getShippingAddress().getCountry()).getStates());
		testForm.setDropshippingAddressStates(countryService.findByIsoCountryCode(testForm.getDropshippingAddress().getCountry()).getStates());
		return mav;
	}

	@RequestMapping(value = "/export_shipments/{shipping_provider_id}", method = RequestMethod.GET)
	public void getFile(@PathVariable("shipping_provider_id") String shippingProviderId,
			HttpServletResponse response) {
		try {
			response.setContentType("text/csv;charset=utf-8");
			response.setHeader("Content-Disposition","attachment; filename=\"" + shippingProviderId + "." + shippingManager.getExportFileExtension(shippingProviderId) + "\"");
			shippingManager.exportShipments(shippingProviderId,
					shipmentService.findAll(),
					new OutputStreamWriter(response.getOutputStream()));
			response.flushBuffer();
		} catch (IOException e) {
			log.error(e.getStackTrace(), e);
		}
	}
	
	private Order convertTestFormToOrder(ShipmentTestForm form) {
		Order order = new Order();
		order.setDropshipment(form.isEnableDropshipping());
		order.getAddresses().add(convertAddressTestFormToAddress(form.getBillingAddress(), AddressType.BillingAddress));
		order.getAddresses().add(convertAddressTestFormToAddress(form.getShippingAddress(), AddressType.ShippingAddress));
		if(form.isEnableDropshipping()) {
			order.getAddresses().add(convertAddressTestFormToAddress(form.getDropshippingAddress(), AddressType.DropShippingAddress));
		}
		return order;
	}

	private Address convertAddressTestFormToAddress(ShipmentAddressTestForm testAddress, AddressType addressType) {
		Address address = new Address(addressType);
		address.setFirstName(testAddress.getFirstName());
		address.setLastName(testAddress.getLastName());
		address.setCompanyName(testAddress.getCompanyName());
		address.setAddressLine1(testAddress.getAddressLine1());
		address.setAddressLine2(testAddress.getAddressLine2());
		address.setCity(testAddress.getCity());
		TCountry country = countryService.findByIsoCountryCode(testAddress.getCountry());
		address.setCountry(country);
		Iterator<TState> stateIter = country.getStates().iterator();
		while(stateIter.hasNext()) {
			TState state = stateIter.next();
			if(testAddress.getState().equals(state.getStateCode())) {
				address.setState(state);
				break;
			}
		}
		address.setZipCode(testAddress.getZipCode());
		return address;
	}
	
	@InitBinder
	protected void initBinder(HttpServletRequest request, WebDataBinder binder) {
		binder.registerCustomEditor(PrototypeProductOptionValue.class, new PropertyEditorSupport() {

			@Override
			public String getAsText() {
				Object value = getValue();
				if(value != null) {
					return ((PrototypeProductOptionValue) value).getId().toString();
				}
				return null;
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				Long id = Long.parseLong(text);
				setValue(prototypeProductOptionValueService.get(id));
			}
			
		});
	}
	*/
}
