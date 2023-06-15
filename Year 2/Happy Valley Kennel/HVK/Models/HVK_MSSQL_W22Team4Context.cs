using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

#nullable disable

namespace HVK.Models
{
    public partial class HVK_MSSQL_W22Team4Context : DbContext
    {
        public HVK_MSSQL_W22Team4Context()
        {
        }

        public HVK_MSSQL_W22Team4Context(DbContextOptions<HVK_MSSQL_W22Team4Context> options)
            : base(options)
        {
        }

        public virtual DbSet<Customer> Customers { get; set; }
        public virtual DbSet<DailyRate> DailyRates { get; set; }
        public virtual DbSet<Discount> Discounts { get; set; }
        public virtual DbSet<Medication> Medications { get; set; }
        public virtual DbSet<Pet> Pets { get; set; }
        public virtual DbSet<PetReservation> PetReservations { get; set; }
        public virtual DbSet<PetReservationDiscount> PetReservationDiscounts { get; set; }
        public virtual DbSet<PetReservationService> PetReservationServices { get; set; }
        public virtual DbSet<PetVaccination> PetVaccinations { get; set; }
        public virtual DbSet<Reservation> Reservations { get; set; }
        public virtual DbSet<ReservationDiscount> ReservationDiscounts { get; set; }
        public virtual DbSet<Run> Runs { get; set; }
        public virtual DbSet<Service> Services { get; set; }
        public virtual DbSet<Vaccination> Vaccinations { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {

                optionsBuilder.UseSqlServer("Server=CSSQL; Database=HVK_MSSQL_W22Team4; User id=W22HVKTEAM4;  Password=password; ");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasAnnotation("Relational:Collation", "SQL_Latin1_General_CP1_CI_AS");

            modelBuilder.Entity<Customer>(entity =>
            {
                entity.ToTable("Customer");

                entity.HasIndex(e => e.CustomerId, "IX_CUSTOMER_PK")
                    .IsUnique();

                entity.Property(e => e.CellPhone)
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.City)
                    .HasMaxLength(25)
                    .IsUnicode(false);

                entity.Property(e => e.Email)
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.EmergencyContactFirstName)
                    .HasMaxLength(25)
                    .IsUnicode(false);

                entity.Property(e => e.EmergencyContactLastName)
                    .HasMaxLength(25)
                    .IsUnicode(false);

                entity.Property(e => e.EmergencyContactPhone)
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.FirstName)
                    .IsRequired()
                    .HasMaxLength(25)
                    .IsUnicode(false);

                entity.Property(e => e.LastName)
                    .IsRequired()
                    .HasMaxLength(25)
                    .IsUnicode(false);

                entity.Property(e => e.Password)
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.Phone)
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.PostalCode)
                    .HasMaxLength(6)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.Province)
                    .HasMaxLength(2)
                    .IsUnicode(false)
                    .HasDefaultValueSql("('QC')")
                    .IsFixedLength(true);

                entity.Property(e => e.Street)
                    .HasMaxLength(40)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<DailyRate>(entity =>
            {
                entity.ToTable("DailyRate");

                entity.HasIndex(e => e.DailyRateId, "IX_DAILY_RATE_PK")
                    .IsUnique();

                entity.Property(e => e.DogSize)
                    .HasMaxLength(1)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.Rate).HasColumnType("numeric(5, 2)");

                entity.HasOne(d => d.Service)
                    .WithMany(p => p.DailyRates)
                    .HasForeignKey(d => d.ServiceId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("DR_SERV_FK");
            });

            modelBuilder.Entity<Discount>(entity =>
            {
                entity.ToTable("Discount");

                entity.HasIndex(e => e.DiscountId, "IX_DISCOUNT_PK")
                    .IsUnique();

                entity.Property(e => e.Desciption)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.Percentage).HasColumnType("numeric(3, 2)");

                entity.Property(e => e.Type)
                    .IsRequired()
                    .HasMaxLength(1)
                    .IsUnicode(false)
                    .HasDefaultValueSql("('D')")
                    .IsFixedLength(true);
            });

            modelBuilder.Entity<Medication>(entity =>
            {
                entity.ToTable("Medication");

                entity.Property(e => e.Dosage)
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.EndDate).HasColumnType("date");

                entity.Property(e => e.Name)
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.SpecialInstruct)
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.HasOne(d => d.PetReservation)
                    .WithMany(p => p.Medications)
                    .HasForeignKey(d => d.PetReservationId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("MED_PR_FK");
            });

            modelBuilder.Entity<Pet>(entity =>
            {
                entity.ToTable("Pet");

                entity.HasIndex(e => e.PetId, "IX_PET_PK")
                    .IsUnique();

                entity.Property(e => e.Breed)
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.DogSize)
                    .HasMaxLength(1)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.Gender)
                    .IsRequired()
                    .HasMaxLength(1)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(25)
                    .IsUnicode(false);

                entity.Property(e => e.SpecialNotes)
                    .HasMaxLength(200)
                    .IsUnicode(false);

                entity.HasOne(d => d.Customer)
                    .WithMany(p => p.Pets)
                    .HasForeignKey(d => d.CustomerId)
                    .HasConstraintName("PET_OWN_FK");
            });

            modelBuilder.Entity<PetReservation>(entity =>
            {
                entity.ToTable("PetReservation");

                entity.HasIndex(e => e.PetReservationId, "IX_PET_RESERVATION_PK")
                    .IsUnique();

                entity.HasOne(d => d.Pet)
                    .WithMany(p => p.PetReservations)
                    .HasForeignKey(d => d.PetId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PR_PET_FK");

                entity.HasOne(d => d.Reservation)
                    .WithMany(p => p.PetReservations)
                    .HasForeignKey(d => d.ReservationId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PR_RES_FK");

                entity.HasOne(d => d.Run)
                    .WithMany(p => p.PetReservations)
                    .HasForeignKey(d => d.RunId)
                    .HasConstraintName("PR_RUN_FK");
            });

            modelBuilder.Entity<PetReservationDiscount>(entity =>
            {
                entity.HasKey(e => new { e.DiscountId, e.PetReservationId })
                    .HasName("PET_RES_DISC_PK");

                entity.ToTable("PetReservationDiscount");

                entity.HasIndex(e => new { e.DiscountId, e.PetReservationId }, "IX_PET_RES_DISC_PK")
                    .IsUnique();

                entity.HasOne(d => d.Discount)
                    .WithMany(p => p.PetReservationDiscounts)
                    .HasForeignKey(d => d.DiscountId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PRD_DISC_FK");

                entity.HasOne(d => d.PetReservation)
                    .WithMany(p => p.PetReservationDiscounts)
                    .HasForeignKey(d => d.PetReservationId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PRD_PR_FK");
            });

            modelBuilder.Entity<PetReservationService>(entity =>
            {
                entity.HasKey(e => new { e.PetReservationId, e.ServiceId })
                    .HasName("PET_RESERVATION_SERVICE_PK");

                entity.ToTable("PetReservationService");

                entity.HasIndex(e => new { e.PetReservationId, e.ServiceId }, "IX_PET_RESERVATION_SERVICE_PK")
                    .IsUnique();

                entity.HasOne(d => d.PetReservation)
                    .WithMany(p => p.PetReservationServices)
                    .HasForeignKey(d => d.PetReservationId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PRS_PR_FK");

                entity.HasOne(d => d.Service)
                    .WithMany(p => p.PetReservationServices)
                    .HasForeignKey(d => d.ServiceId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PRS_SERV_FK");
            });

            modelBuilder.Entity<PetVaccination>(entity =>
            {
                entity.HasKey(e => new { e.VaccinationId, e.PetId })
                    .HasName("PET_VACCINATION_PK");

                entity.ToTable("PetVaccination");

                entity.HasIndex(e => new { e.VaccinationId, e.PetId }, "IX_PET_VACCINATION_PK")
                    .IsUnique();

                entity.Property(e => e.ExpiryDate).HasColumnType("date");

                entity.HasOne(d => d.Pet)
                    .WithMany(p => p.PetVaccinations)
                    .HasForeignKey(d => d.PetId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PV_PET_FK");

                entity.HasOne(d => d.Vaccination)
                    .WithMany(p => p.PetVaccinations)
                    .HasForeignKey(d => d.VaccinationId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("PV_VACC_FK");
            });

            modelBuilder.Entity<Reservation>(entity =>
            {
                entity.ToTable("Reservation");

                entity.HasIndex(e => e.ReservationId, "IX_RESERVATION_PK")
                    .IsUnique();

                entity.Property(e => e.EndDate).HasColumnType("date");

                entity.Property(e => e.StartDate).HasColumnType("date");

                entity.Property(e => e.Status)
                    .HasColumnType("numeric(1, 0)")
                    .HasDefaultValueSql("((2))");
            });

            modelBuilder.Entity<ReservationDiscount>(entity =>
            {
                entity.HasKey(e => new { e.DiscountId, e.ReservationId })
                    .HasName("RES_DISC_PK");

                entity.ToTable("ReservationDiscount");

                entity.HasIndex(e => new { e.DiscountId, e.ReservationId }, "IX_RES_DISC_PK")
                    .IsUnique();

                entity.HasOne(d => d.Discount)
                    .WithMany(p => p.ReservationDiscounts)
                    .HasForeignKey(d => d.DiscountId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("RD_DISC_FK");

                entity.HasOne(d => d.Reservation)
                    .WithMany(p => p.ReservationDiscounts)
                    .HasForeignKey(d => d.ReservationId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("RD_RES_FK");
            });

            modelBuilder.Entity<Run>(entity =>
            {
                entity.ToTable("Run");

                entity.HasIndex(e => e.RunId, "IX_RUN_PK")
                    .IsUnique();

                entity.Property(e => e.Location)
                    .HasMaxLength(1)
                    .IsUnicode(false)
                    .IsFixedLength(true);

                entity.Property(e => e.Size)
                    .IsRequired()
                    .HasMaxLength(1)
                    .IsUnicode(false)
                    .HasDefaultValueSql("('R')")
                    .IsFixedLength(true);

                entity.Property(e => e.Status)
                    .HasColumnType("numeric(1, 0)")
                    .HasDefaultValueSql("((1))");
            });

            modelBuilder.Entity<Service>(entity =>
            {
                entity.ToTable("Service");

                entity.HasIndex(e => e.ServiceId, "IX_SERVICE_PK")
                    .IsUnique();

                entity.Property(e => e.ServiceDescription)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<Vaccination>(entity =>
            {
                entity.ToTable("Vaccination");

                entity.HasIndex(e => e.VaccinationId, "IX_VACCINATION_PK")
                    .IsUnique();

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false);
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
