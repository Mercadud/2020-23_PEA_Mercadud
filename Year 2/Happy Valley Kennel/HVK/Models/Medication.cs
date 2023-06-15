using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

#nullable disable

namespace HVK.Models
{
    public partial class Medication
    {
        public int MedicationId { get; set; }
        public string Name { get; set; }
        public string Dosage { get; set; }
        [Display(Name = "Instruction:")]
        [DataType(DataType.MultilineText)]
        public string SpecialInstruct { get; set; }
        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        public DateTime? EndDate { get; set; }
        public int PetReservationId { get; set; }

        public virtual PetReservation PetReservation { get; set; }
    }
}
