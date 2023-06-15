using HVK.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HVK.Controllers
{
    public class ReservationsController : Controller
    {
        private readonly HVK_MSSQL_W22Team4Context _context;


        public ReservationsController(HVK_MSSQL_W22Team4Context context)
        {
           
            _context = context;
        }

        //list
       

        public ActionResult Details(int id)
        {
           ViewBag.Reservations = _context.PetReservations
                .Include(p => p.Reservation)
                .Include(p => p.Pet)
                .Where(p => p.ReservationId == id)
                .FirstOrDefault();
            return View(_context.Customers.FirstOrDefault());
        }

        [HttpGet, Route("Create")]
        public IActionResult Create(int? id)
        {
            if (id == null)
                id = 7;
            var pets = _context.Pets.Where(p => p.CustomerId == id.Value).ToList();

            ViewData["Pets"] = _context.Pets.Where(p => p.CustomerId == id.Value).ToList();
           
            ViewData["Services"] = _context.Services.Where(p => p.ServiceId != 1).ToList();
         


            return View();
        }


        [HttpPost, Route("Create")]
        public IActionResult Create(int? id, PetReservation model)
        {
            int ownerId = 7;
            int PetId = model.PetId;

            ViewData["Pets"] = _context.Pets.Where(p => p.CustomerId == ownerId).ToList();
            ViewData["Services"] = _context.Services.Where(p => p.ServiceId != 1).ToList();

            if (!ModelState.IsValid)
            {
                return View(model);
            }
            else
            {
                try
                {
                    var owner = _context.Customers.Where(o => o.CustomerId == ownerId);
                    model.Pet = _context.Pets.Where(p => p.PetId == PetId).FirstOrDefault();

                    model.Pet.CustomerId= ownerId;

                    _context.PetReservations.Add(model);
                    _context.SaveChanges();
                    return RedirectToAction(nameof(Index));
                }
                catch
                {
                    return View(model);
                }
            }
        }

        [HttpGet, Route("Edit")]
        public ActionResult Edit(int? id)
        {

            ViewData["Services"] = _context.Services.Where(p => p.ServiceId != 1).ToList();
            ViewData["PetServices"] = _context.PetReservationServices
                                        .Where(p => p.PetReservationId == id).ToList();

            var model = _context.PetReservations
                .Include(p => p.Reservation)
                .Include(p => p.Pet)
                .Where(p => p.PetReservationId == id)
                .FirstOrDefault();

            return View(model);
        }

        [HttpPost, Route("Edit")]
        public ActionResult Edit(int id, PetReservation model)
        {
            ViewData["Services"] = _context.Services.Where(p => p.ServiceId != 1).ToList();
            ViewData["PetServices"] = _context.PetReservationServices
                                .Where(p => p.PetReservationId == id).ToList();

            if (!ModelState.IsValid)
            {
                return View(model);
            }
            else
            {
                try
                {
                    _context.PetReservations.Update(model);
                    _context.SaveChanges();

                    return RedirectToAction(nameof(Index));
                }
                catch
                {
                    return View(model);
                }
            }
        }


        //FIX ADD Medication for RESERVATION


        

        [Route("Delete")]
        public ActionResult Delete(int? id)
        {
            return View();
        }

        [HttpPost, Route("Delete")]
        public ActionResult Delete(int id)
        {
            try
            {
                PetReservation petRes = _context.PetReservations.Find(id);
                _context.Remove(petRes);
                _context.SaveChanges();
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return RedirectToAction(nameof(Index));
            }
        }
    }


}



