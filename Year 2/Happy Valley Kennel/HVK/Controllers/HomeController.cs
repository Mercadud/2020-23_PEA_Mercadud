
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using HVK.Models;
using Microsoft.AspNetCore.Http;

namespace HVK.Controllers {
    public class HomeController : Controller {
        //private readonly ILogger<HomeController> _logger;

        //public HomeController(ILogger<HomeController> logger) {
        //    _logger = logger;
        //}

        private readonly HVK_MSSQL_W22Team4Context _context;

        public HomeController(HVK_MSSQL_W22Team4Context context) {
            _context = context;
        }

        [HttpGet]
        public IActionResult Index() {
            return View();
        }

        [HttpPost]
        public IActionResult Index(Login l) {
            if (ModelState.IsValid) {
                Customer cust = _context.Customers.Where(t => t.Email == l.Email).FirstOrDefault();
                HttpContext.Session.SetInt32("Id", cust.CustomerId);
                if (cust.CustomerId == 21) {
                    return Redirect("/Clerks");
                }
               
                return Redirect("/Customers/Index");
            }
            return View(l);
        }


        //Show the sign up page to the customer
        public IActionResult SignUp() {
            return View();
        }

        //[HttpPost]
        //[ValidateAntiForgeryToken]
        //public async Task<IActionResult> SignUp(
        //   [Bind(
        //        "CustomerId,FirstName,LastName,Street,City,Province,PostalCode,Phone,CellPhone,Email,EmergencyContactFirstName,EmergencyContactLastName,EmergencyContactPhone,Password")]
        //    Customer customer) {
        //    if (ModelState.IsValid) {
        //        _context.Add(customer);
        //        await _context.SaveChangesAsync();
        //        return RedirectToAction(nameof(Index));
        //    }

        //    return View(customer);
        //}

        public IActionResult Privacy() {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error() {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
