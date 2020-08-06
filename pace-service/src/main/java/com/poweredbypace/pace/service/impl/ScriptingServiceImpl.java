package com.poweredbypace.pace.service.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.CustomScript;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.print.BinderyFormGenerator;
import com.poweredbypace.pace.print.LayoutPrintGenerator;
import com.poweredbypace.pace.print.jpeg.GhostscriptPdfToJpegConverter;
import com.poweredbypace.pace.print.pdf.HiResPdfRenderer;
import com.poweredbypace.pace.print.pdf.HiResPrintsPdfRenderer;
import com.poweredbypace.pace.print.pdf.HiResSpreadPdfRenderer;
import com.poweredbypace.pace.repository.AttachmentRepository;
import com.poweredbypace.pace.repository.CustomScriptRepository;
import com.poweredbypace.pace.repository.GenericRuleRepository;
import com.poweredbypace.pace.repository.InvoiceRepository;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.DieService;
import com.poweredbypace.pace.service.EmailService;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.IccProfileService;
import com.poweredbypace.pace.service.InvoiceService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.ProoferService;
import com.poweredbypace.pace.service.ScriptingService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.service.ViewService;

@Service
public class ScriptingServiceImpl implements ScriptingService {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private InvoiceRepository invoiceRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private CustomScriptRepository scriptRepo;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private DieService dieService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired(required=false)
	@Qualifier("pdfGenerator")
	private LayoutPrintGenerator pdfGen;
	
	@Autowired(required=false)
	@Qualifier("jpegGenerator")
	private LayoutPrintGenerator jpegGen;
	
	@Autowired(required=false)
	@Qualifier("jpegFolderGenerator")
	private LayoutPrintGenerator jpegFolderGen;
	
	@Autowired(required=false)
	private BinderyFormGenerator bfGen;
	
	@Autowired
	private Env env;
	
	@Autowired
	private ViewService viewService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private GenericRuleRepository genericRuleRepository;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	public NotificationBroadcaster notificationBroadcaster;
	
	@Autowired(required=false)
	private HiResPdfRenderer hiResPdfRenderer;
	
	@Autowired(required=false)
	private HiResSpreadPdfRenderer hiResSpreadPdfRenderer;
	
	@Autowired(required=false)
	private HiResPrintsPdfRenderer hiResPrintsPdfRenderer;
	
	@Autowired(required=false)
	private GhostscriptPdfToJpegConverter ghostscriptPdfToJpegConverter;
	
	@Autowired(required=false)
	private IccProfileService iccService;
	
	@Autowired
	private AttachmentRepository attachmentRepo;
	
	@Autowired
	private ProoferService prooferService;
	
	
	@Override
	public void runScript(String script, Map<String,Object> params) {
		this.runScript(script, params, String.class);
	}

	@Override
	public void runScript(String script) {
		this.runScript(script, null, String.class);
	}
	
	@Override
	public <T> T runScript(String script, Map<String,Object> params, Class<T> resultType) {
		Context context = Context.enter();
		context.setOptimizationLevel(-1);
		context.setLanguageVersion(Context.VERSION_ES6);
		
		try {
			Scriptable scope = initScope(context, params);
			Object result = context.evaluateString(scope, script, "PACE_SCRIPT", 1, null);
			return resultType.cast( Context.jsToJava(result, resultType) );
		} finally {
			Context.exit();
		}
	}
	
	private Scriptable initScope(Context context, Map<String,Object> params) {
		Scriptable scope = new ImporterTopLevel(context);
		ScriptableObject.putProperty(scope, "BinderyFormGenerator", Context.javaToJS(bfGen, scope));
		ScriptableObject.putProperty(scope, "PdfGenerator", Context.javaToJS(pdfGen, scope));
		ScriptableObject.putProperty(scope, "JpegGenerator", Context.javaToJS(jpegFolderGen, scope));
		ScriptableObject.putProperty(scope, "ProductRepository", Context.javaToJS(productRepo, scope));
		ScriptableObject.putProperty(scope, "OrderRepository", Context.javaToJS(orderRepo, scope));
		ScriptableObject.putProperty(scope, "InvoiceService", Context.javaToJS(invoiceService, scope));
		ScriptableObject.putProperty(scope, "DieService", Context.javaToJS(dieService, scope));
		ScriptableObject.putProperty(scope, "LayoutService", Context.javaToJS(layoutService, scope));
		ScriptableObject.putProperty(scope, "StorageService", Context.javaToJS(storageService, scope));
		ScriptableObject.putProperty(scope, "PricingService", Context.javaToJS(pricingService, scope));
		ScriptableObject.putProperty(scope, "ProductService", Context.javaToJS(productService, scope));
		ScriptableObject.putProperty(scope, "OrderService", Context.javaToJS(orderService, scope));
		ScriptableObject.putProperty(scope, "ViewService", Context.javaToJS(viewService, scope));
		ScriptableObject.putProperty(scope, "RuleService", Context.javaToJS(ruleService, scope));
		ScriptableObject.putProperty(scope, "EmailService", Context.javaToJS(emailService, scope));
		ScriptableObject.putProperty(scope, "EventService", Context.javaToJS(eventService, scope));
		ScriptableObject.putProperty(scope, "IccProfileService", Context.javaToJS(iccService, scope));
		ScriptableObject.putProperty(scope, "ProoferService", Context.javaToJS(prooferService, scope));
		ScriptableObject.putProperty(scope, "ExpressionEvaluator", Context.javaToJS(expressionEvaluator, scope));
		ScriptableObject.putProperty(scope, "Env", Context.javaToJS(env, scope));
		ScriptableObject.putProperty(scope, "InvoiceRepository", Context.javaToJS(invoiceRepo, scope));
		ScriptableObject.putProperty(scope, "NotificationBroadcaster", Context.javaToJS(notificationBroadcaster, scope));
		ScriptableObject.putProperty(scope, "HiResPdfRenderer", Context.javaToJS(hiResPdfRenderer, scope));
		ScriptableObject.putProperty(scope, "HiResSpreadPdfRenderer", Context.javaToJS(hiResSpreadPdfRenderer, scope));
		ScriptableObject.putProperty(scope, "GenericRuleRepository", Context.javaToJS(genericRuleRepository, scope));
		ScriptableObject.putProperty(scope, "AttachmentRepository", Context.javaToJS(attachmentRepo, scope));
		ScriptableObject.putProperty(scope, "HiResPrintsPdfRenderer", Context.javaToJS(hiResPrintsPdfRenderer, scope));
		ScriptableObject.putProperty(scope, "GhostscriptPdfToJpegConverter", Context.javaToJS(ghostscriptPdfToJpegConverter, scope));
		ScriptableObject.putProperty(scope, "log", Context.javaToJS(log, scope));
		ScriptableObject.putProperty(scope, "CustomScriptImporter", Context.javaToJS(new CustomScriptImporter(context, scope, scriptRepo), scope));
		
		if (params!=null) {
			@SuppressWarnings("serial")
			ScriptableObject paramsJs = new ScriptableObject() {
				@Override public String getClassName() { return null; }
			};
			for(String key:params.keySet()) {
				paramsJs.defineProperty(key, params.get(key), ScriptableObject.READONLY);
			}
			ScriptableObject.putProperty(scope, "params", paramsJs);
		}
		
		return scope;
	}

	public static class CustomScriptImporter {
		private CustomScriptRepository repo;
		private Context ctx;
		private Scriptable scope;
		
		public CustomScriptImporter(Context ctx, Scriptable scope, CustomScriptRepository repo) {
			this.repo = repo;
			this.ctx = ctx;
			this.scope = scope;
		}
		
		public void importScript(String code) {
			CustomScript s = repo.findByCode(code);
			ctx.evaluateString(scope, s.getScript(), code, 1, null);
		}
	}
}
