using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using HVK.Models;
using Microsoft.AspNetCore.Http;

namespace HVK.Controllers
{
    public class PetsController : Controller
    {
        private readonly HVK_MSSQL_W22Team4Context _context;

        public PetsController(HVK_MSSQL_W22Team4Context context)
        {
            _context = context;
        }

        // GET: Pets
        public IActionResult Index(int id)
        {
            //it should be changed to be the current customer's id
            int userId = 2;

            ViewBag.Vaccinations = _context.Vaccinations.ToList();

            var result = _context.Pets
                .Include(p => p.PetVaccinations)
                .ThenInclude(v => v.Vaccination)
                .Where(p => p.CustomerId == userId);

            return View(result.ToList());
        }

        [HttpGet]
        public IActionResult Edit(int id)
        {
            return UsefulFunctions.EzView(this, _context, Custid =>
            {
                ViewBag.pet = _context.Pets.Where(p => p.PetId == id)
                .Include(p => p.PetVaccinations)
                .ThenInclude(v => v.Vaccination)
                .FirstOrDefault();
            });
        }

        [HttpPost]
        public IActionResult Edit(Pet pet)
        {
            if (!ModelState.IsValid)
            {
                return (ActionResult)UsefulFunctions.EzView(this, _context, id =>
                {
                    ViewBag.pet = _context.Pets.Where(p => p.PetId == pet.PetId)
                        .Include(p => p.PetVaccinations)
                        .FirstOrDefault();
                });
            }
            else
            {
                var p = _context.Pets.Where(p => p.PetId == pet.PetId).FirstOrDefault();
                p.Name = pet.Name;
                p.Breed = pet.Breed;
                p.Birthyear = pet.Birthyear;
                p.Gender = pet.Gender;
                p.DogSize = pet.DogSize;
                p.Climber = pet.Climber;
                p.Barker = pet.Barker;
                p.SpecialNotes = pet.SpecialNotes;

                _context.SaveChanges();
                return RedirectToAction("Index", "Customers");
            }
        }

        [HttpGet]
        public IActionResult Create()
        {
            return (ActionResult)UsefulFunctions.EzView(this, _context, id =>
            {
                ViewBag.Pet = new Pet { };
            });
        }

        [HttpPost]
        public IActionResult Create(Pet pet, IFormCollection collection)
        {
            if (!ModelState.IsValid)
            {
                return (ActionResult)UsefulFunctions.EzView(this, _context, id =>
                {
                    ViewBag.Pet = pet;
                });
            }
            else
            {
                pet.CustomerId = HttpContext.Session.GetInt32("Id").Value;
                var vaccinations = _context.Vaccinations.ToList();
                foreach (var i in vaccinations)
                {
                    pet.PetVaccinations.Add(new PetVaccination
                    {
                        PetId = pet.PetId,
                        VaccinationId = i.VaccinationId,
                        ExpiryDate = DateTime.Today,
                        VaccinationChecked = false
                    });
                }
                _context.Pets.Add(pet);
                _context.SaveChanges();
                return RedirectToAction("Index", "Customers");
            }
        }

        public IActionResult Delete(int id)
        {
            Console.WriteLine(_context.Pets.Where(t => t.PetId == id));
            var pet = _context.Pets
                .Where(t => t.PetId == id && t.CustomerId == HttpContext.Session.GetInt32("Id").Value)
                .FirstOrDefault();
            if (pet != null)
            {
                foreach (var i in _context.PetVaccinations.Where(t => t.PetId == id))
                {
                    _context.PetVaccinations.Remove(i);
                }
                _context.Pets.Remove(pet);
                _context.SaveChanges();
            }
            return RedirectToAction("Index", "Customers");
        }

        [HttpPost]
        public IActionResult EditPetVaccination(PetVaccination p)
        {
            if (ModelState.GetFieldValidationState("ExpiryDate") == Microsoft.AspNetCore.Mvc.ModelBinding.ModelValidationState.Invalid) {
                return RedirectToAction("Edit", "Pets", p.PetId);
             }
            var vax = _context.PetVaccinations.Where(t => t.VaccinationId == p.VaccinationId && t.PetId == p.PetId).FirstOrDefault();
            vax.ExpiryDate = p.ExpiryDate;
            vax.VaccinationChecked = false;
            _context.SaveChanges();
            return RedirectToAction("Edit", "Pets", new { id = p.PetId });
        }
    }
}
