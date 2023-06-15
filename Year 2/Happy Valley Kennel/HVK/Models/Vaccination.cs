using System;
using System.Collections.Generic;

#nullable disable

namespace HVK.Models
{
    public partial class Vaccination
    {
        public Vaccination()
        {
            PetVaccinations = new HashSet<PetVaccination>();
        }

        public int VaccinationId { get; set; }
        public string Name { get; set; }

        public virtual ICollection<PetVaccination> PetVaccinations { get; set; }
    }
}
