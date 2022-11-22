UPDATE pace_lta_dev.APP_STORE
SET VERSION=1, BASE_CURRENCY='CAD', DOMAIN_NAME='pace-app-dev-1564346246.us-west-2.elb.amazonaws.com', IS_DEFAULT=1, NAME='D&R Lifethreads Albums Store', OWNER_EMAIL='support@lifethreadsalbums.com', STORAGE_URL='https://lifethreadsalbums-dev-env.s3.amazonaws.com/', ADDRESS_ID=1, CONFIG_JSON='{
  "appCode": "PACE-LTA",
  "appTitle": "Lifethreads Studio",
  "prooferUrl": "http://pace-app-dev-1564346246.us-west-2.elb.amazonaws.com/",
  "prooferOverviewUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/Proofer_Overview.mp4",
  "notificationServerUrl": "https://drpush.poweredbypace.com/studio",
  "eulaUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/eula.html",
  "termsUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/terms.html",
  "privacyUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/privacy.html",
  "tourUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/tour.html",
  "tourBypassLimit": 3,
  "helpDeskUrl": "http://pace-app-dev-1564346246.us-west-2.elb.amazonaws.com/",
  "loginPage": {
    "logo": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/logo-lta.png",
    "logoWidth": 400,
    "logoUrl": "http://pace-app-dev-1564346246.us-west-2.elb.amazonaws.com",
    "paceLogo": true
  },
  "prints": {
    "visible": true,
    "newProductUrl": "build/new/details/product-type?prototypeId=11"
  },
  "logo": {
    "url": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/logo-lta-small.png",
    "height": 41,
    "text": "Lifethreads"
  },
  "cameoBleed": 9,
  "urlPrefix": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/",
  "imageUrlPrefix": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/images/",
  "defaultMaterialUrl": "https://irisstudio.s3.amazonaws.com/materials/default-material.jpg",
  "studioSampleDie": {
    "url": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/lta_sample.png",
    "width": 730,
    "height": 178
  },
  "maxNumberOfStampLines": 2,
  "coverBuilder": {
    "defaultFontFamily": "''TWCRegular''",
    "defaultFontSize": "48",
    "strictFontSizes": false
  },
  "adminOrders": {
    "contextMenu": {
      "generate": {
        "generateJpegCover": {
          "label": "JPEG Cover",
          "cover": true
        },
        "generateHiResJpeg": {
          "label": "JPEG Spreads"
        },
        "sep": {
          "separator": true
        },
        "generateHiResPdf": {
          "label": "PDF"
        },
        "generatePdfCover": {
          "label": "PDF Cover",
          "cover": true
        },
        "generateLowResPdf": {
          "label": "PDF Proof"
        },
        "sep2": {
          "separator": true
        },
        "generateHiResTiff": {
          "label": "TIF Spreads"
        },
        "generateTiffCover": {
          "label": "TIF Cover",
          "cover": true
        },
        "sep3": {
          "separator": true
        },
        "cameo": {
          "label": "Cameo",
          "task": "{type:''com.poweredbypace.pace.job.task.GenerateCameosTask'', params:{class:''com.poweredbypace.pace.job.task.GenerateCameosTask$Params'', productId: product.id, outputType:''Jpeg''}}"
        },
        "sep4": {
          "separator": true
        },
        "engraving": {
          "label": "Engraving",
          "task": "{type:''com.poweredbypace.pace.job.task.GenerateDiesTask'', params:{class:''com.poweredbypace.pace.job.task.GenerateDiesTask$Params'', productId: product.id, outputType:''Png''}}"
        },
        "sep5": {
          "separator": true
        },
        "genPrints": {
          "label": "Prints",
          "task": "{type:''com.poweredbypace.pace.job.task.CustomScriptTask'', params:{class:''com.poweredbypace.pace.job.task.CustomScriptTaskImpl$Params'', timeout:14400, scriptId:''GEN_SPO'', data: {productId: product ? product.id : null, orderId: order.id} } }"
        }
      }
    },
    "projects": [
      {
        "field": "productStatus",
        "header": "Status"
      },
      {
        "field": "productDate",
        "header": "Created"
      },
      {
        "field": "user",
        "header": "User"
      },
      {
        "field": "company",
        "header": "Company"
      },
      {
        "optionCode": "_name",
        "header": "Project"
      },
      {
        "optionCode": "_productPrototype",
        "header": "Product Type"
      },
      {
        "optionCode": "size",
        "header": "Shape"
      },
      {
        "optionCode": "material",
        "header": "Material"
      },
      {
        "field": "studioSample",
        "header": "Sample"
      },
      {
        "field": "price",
        "header": "Price"
      },
      {
        "field": "currency",
        "header": "$"
      },
      {
        "field": "orderState",
        "header": "Payment"
      }
    ],
    "orders": [
      {
        "field": "productStatus",
        "header": "Status"
      },
      {
        "field": "orderDate",
        "header": "Ordered"
      },
      {
        "field": "orderId",
        "header": "Order ID"
      },
      {
        "field": "rush",
        "header": "Rush"
      },
      {
        "field": "dueDate",
        "header": "Ship by"
      },
      {
        "field": "quantity",
        "header": "Qty"
      },
      {
        "field": "user",
        "header": "User"
      },
      {
        "field": "company",
        "header": "Company"
      },
      {
        "optionCode": "_name",
        "header": "Project"
      },
      {
        "optionCode": "_productPrototype",
        "header": "Product Type"
      },
      {
        "optionCode": "size",
        "header": "Shape"
      },
      {
        "optionCode": "material",
        "header": "Material"
      },
      {
        "field": "studioSample",
        "header": "Sample"
      },
      {
        "field": "productDate",
        "header": "Created"
      },
      {
        "field": "price",
        "header": "Price"
      },
      {
        "field": "currency",
        "header": "$"
      },
      {
        "field": "orderState",
        "header": "Payment"
      },
      {
        "field": "attachments",
        "header": "Files"
      },
      {
        "field": "jobProgress",
        "header": "Progress"
      }
    ],
    "currentBatch": [
      {
        "field": "productStatus",
        "header": "Status"
      },
      {
        "field": "orderDate",
        "header": "Ordered"
      },
      {
        "field": "currentBatchId",
        "header": "Batch ID"
      },
      {
        "field": "orderId",
        "header": "Order ID"
      },
      {
        "field": "user",
        "header": "User"
      },
      {
        "optionCode": "_name",
        "header": "Project"
      },
      {
        "optionCode": "_productPrototype",
        "header": "Product Type"
      },
      {
        "optionCode": "size",
        "header": "Shape"
      },
      {
        "optionCode": "material",
        "header": "Material"
      },
      {
        "optionCode": "paperType",
        "header": "Paper"
      },
      {
        "optionCode": "_quantity",
        "header": "Sets"
      },
      {
        "optionCode": "_pageCount",
        "header": "Pages"
      },
      {
        "field": "attachments",
        "header": "Files"
      },
      {
        "field": "jobProgress",
        "header": "Progress"
      }
    ],
    "shipped": [
      {
        "field": "productStatus",
        "header": "Status"
      },
      {
        "field": "orderDate",
        "header": "Ordered"
      },
      {
        "field": "orderId",
        "header": "Order ID"
      },
      {
        "optionCode": "carrier",
        "header": "Carrier",
        "editable": true,
        "editor": "dropdown"
      },
      {
        "optionCode": "trackingId",
        "header": "Tracking ID",
        "editable": true,
        "editor": "text"
      },
      {
        "optionCode": "dateShipped",
        "header": "Shipped",
        "editable": true,
        "editor": "date"
      },
      {
        "optionCode": "dateDelivered",
        "header": "Delivered",
        "editable": true,
        "editor": "date"
      },
      {
        "optionCode": "_quantity",
        "header": "Qty"
      },
      {
        "field": "user",
        "header": "User"
      },
      {
        "field": "company",
        "header": "Company"
      },
      {
        "optionCode": "_name",
        "header": "Project"
      },
      {
        "optionCode": "_productPrototype",
        "header": "Product Type"
      },
      {
        "optionCode": "size",
        "header": "Shape"
      },
      {
        "optionCode": "material",
        "header": "Material"
      },
      {
        "field": "studioSample",
        "header": "Sample"
      },
      {
        "field": "productDate",
        "header": "Created"
      },
      {
        "field": "price",
        "header": "Price"
      },
      {
        "field": "currency",
        "header": "$"
      },
      {
        "field": "orderState",
        "header": "Payment"
      },
      {
        "field": "jobProgress",
        "header": "Progress"
      }
    ],
    "completed": [
      {
        "field": "productStatus",
        "header": "Status"
      },
      {
        "field": "orderDate",
        "header": "Ordered"
      },
      {
        "field": "orderId",
        "header": "Order ID"
      },
      {
        "optionCode": "carrier",
        "header": "Carrier",
        "editable": true,
        "editor": "dropdown"
      },
      {
        "optionCode": "trackingId",
        "header": "Tracking ID",
        "editable": true,
        "editor": "text"
      },
      {
        "optionCode": "datePrinted",
        "header": "Printed"
      },
      {
        "optionCode": "dateShipped",
        "header": "Shipped",
        "editable": true,
        "editor": "date"
      },
      {
        "optionCode": "dateDelivered",
        "header": "Delivered",
        "editable": true,
        "editor": "date"
      },
      {
        "optionCode": "_quantity",
        "header": "Qty"
      },
      {
        "field": "user",
        "header": "User"
      },
      {
        "field": "company",
        "header": "Company"
      },
      {
        "optionCode": "_name",
        "header": "Project"
      },
      {
        "optionCode": "_productPrototype",
        "header": "Product Type"
      },
      {
        "optionCode": "size",
        "header": "Shape"
      },
      {
        "optionCode": "material",
        "header": "Material"
      },
      {
        "field": "studioSample",
        "header": "Sample"
      },
      {
        "field": "productDate",
        "header": "Created"
      },
      {
        "field": "price",
        "header": "Price"
      },
      {
        "field": "currency",
        "header": "$"
      },
      {
        "field": "orderState",
        "header": "Payment"
      },
      {
        "field": "jobProgress",
        "header": "Progress"
      }
    ],
    "gridContext": {
      "irisPages": [
        "function(product) {                                                             ",
        "    var pageCount = product.options._pageCount;                                 ",
        "    if (product.isReprint) {                                                    ",
        "        var pages = PACE.utils.parsePageNumbers(product.options._reprintPages); ",
        "        pageCount = pages.length;                                               ",
        "    }                                                                           ",
        "    return pageCount;                                                           ",
        "}                                                                               "
      ]
    }
  },
  "welcomePage": {
    "title": "Welcome to Lifethreads!",
    "products": [
      {
        "name": "Flush Mounts",
        "thumbUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/builder/welcome/welcome_thumb_flush%20mount.jpg",
        "url": "/build/new/details/product-type?prototypeId=3"
      },
      {
        "name": "Signing Books",
        "thumbUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/builder/welcome/welcome_thumb_signing%20book.jpg",
        "url": "/build/new/details/product-type?prototypeId=4"
      },
      {
        "name": "Prints",
        "thumbUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/builder/welcome/welcome_thumb_prints.jpg",
        "url": "build/new/details/product-type?prototypeId=11"
      },
      {
        "name": "Wall Displays",
        "thumbUrl": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/builder/welcome/welcome_thumb_canvas.jpg",
        "url": "build/new/details/product-type?prototypeId=12"
      },
      {
        "name": "Custom Services",
        "thumbUrl": "images/welcome-custom-services.png",
        "url": "/orders/new?prototypeId=2"
      }
    ]
  },
  "dashboard": {
    "buttons": [
      {
        "label": "See the Blog",
        "title": "Get Important Updates, News, Announcements on our blog",
        "url": "https://www.lifethreadsalbums.com/the-blog/"
      },
      {
        "label": "Subscribe to Email List",
        "title": "Subscribe & save 15% on your first order",
        "url": "http://eepurl.com/k8I0j"
      }
    ],
    "promotions": [
      {
        "background": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/dashboard/Dashboard_image%20header_sample%20kit.jpg",
        "subtitle": "welcome to",
        "title": "lifethreads studio",
        "body": "We just made it way easier to design, order and yes sell albums!"
      },
      {
        "background": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/dashboard/studio_header_proofer.jpg",
        "subtitle": "album proofing done right",
        "title": "proofer is live",
        "body": "intuitive clear communication. take all your albums from concept to completion."
      },
      {
        "background": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/dashboard/Studio_header_prints.jpg",
        "subtitle": "one-stop shop",
        "title": "order prints with ease",
        "body": "from wallets to 24x36 giclee prints we''ve got you covered.  all in your lifethreads studio app."
      },
      {
        "background": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/dashboard/dashboard_iamge%20header_album%20%2B%20kraft%20box.jpg",
        "subtitle": "unrivalled speed | uncompromising quality",
        "title": "fantastic & fast",
        "body": "Most albums are ready to ship in about a week."
      },
      {
        "background": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/dashboard/Dashboard_header_bamboo%20copy.jpg",
        "subtitle": "meet the",
        "title": "new kid on the block",
        "body": "Our new eco-friendly bamboo cover albums are the perfect blend of luxury and eco-friendliness. Get yours today."
      }
    ],
    "messages": [
      {
        "from": "2016-12-03 06:59:59",
        "to": "2016-12-26 06:59:59",
        "message": "Please note that D&R will be closed from Thursday December 22nd 2016 and reopen on Tuesday January 3rd 2017. Thank You for a great 2016 and have a great holiday season!"
      },
      {
        "from": "2017-05-01 00:00:00",
        "to": "2017-05-31 00:00:00",
        "message": "FREE Canadian Standard shipping for the month of May! ($15 Value - Orders must be $100 or more before taxes)",
        "type": "info"
      },
      {
        "from": "2017-09-01 00:00:00",
        "to": "2017-09-30 00:00:00",
        "message": "On now: save 10% on all 14x11, 12x12 & 9x12 albums! Applies to all cover styles and add-ons. Discounts will be applied automatically.<br>Promotion ends midnight September 30, 2017",
        "type": "info"
      },
      {
        "from": "2018-04-30 00:00:00",
        "to": "2018-06-01 00:00:00",
        "message": "PROOFER is live now in beta! Clear & intuitive album proofing that is fully integrated with lifethreads studio album designer.  We''re taking the headache out of client album proofing.  Best part - it''s 100% free.",
        "type": "info"
      }
    ],
    "priceBox": {
      "title": "Pricing, Promotions & Shipping",
      "items": [
        {
          "title": "Current Price Guide",
          "description": "Looking for a detailed breakdown of all the product options? Download our latest price list.",
          "url": "https://lifethreadsalbums-dev-env.s3.amazonaws.com/assets/lta_price_general-list_2022.pdf"
        }
      ]
    }
  }
}', CREATED=NULL, MODIFIED=NULL, CODE='lifethreadsalbums'
WHERE ID=1;
