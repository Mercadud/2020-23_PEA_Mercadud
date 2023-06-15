using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using HVK.Models;

namespace HVK.Controllers {
    public class EndPetVisitController : Controller {

        private readonly HVK_MSSQL_W22Team4Context _context;

        public EndPetVisitController(HVK_MSSQL_W22Team4Context context) {
            _context = context;
        }
        public IActionResult Index() {
            return View(_context.Customers.Include(c => c.Pets).FirstOrDefault());
        }

        public IActionResult Invoice() {
            return View(_context.Customers.Include(c => c.Pets).FirstOrDefault());
        }

    }
}
